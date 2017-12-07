package com.mengka.springboot.karaf_01;

import org.apache.karaf.features.BootFinished;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.junit.Before;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.MavenUtils.asInProject;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

public class TestBase {

    static final Long COMMAND_TIMEOUT = 30000L;
    static final Long SERVICE_TIMEOUT = 30000L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    protected BundleContext bc;

    @Inject
    protected FeaturesService featuresService;

    @Inject
    protected SessionFactory sessionFactory;

    /**
     * To make sure the tests run only when the boot features are fully
     * installed
     */
    @Inject
    protected BootFinished bootFinished;

    private ExecutorService executor = Executors.newCachedThreadPool();

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private PrintStream errStream = new PrintStream(byteArrayOutputStream);
    private Session session;

    public Option[] configBase() {
        return options(
                karafDistributionConfiguration().frameworkUrl(mvnKarafDist())
                        .unpackDirectory(new File("target/paxexam/unpack/")).useDeployFolder(false),
                logLevel(LogLevel.INFO), keepRuntimeFolder(), features(karafStandardFeature(), "scr"),
                configureConsole().ignoreLocalConsole(), junitBundles());
    }

    private MavenArtifactUrlReference mvnKarafDist() {
        return maven().groupId("de.nierbeck.microservices.karaf.tools").artifactId("Karaf-Service-Runtime")
                .type("tar.gz").version(asInProject());
    }

    private MavenUrlReference karafStandardFeature() {
        return maven().groupId("org.apache.karaf.features").artifactId("standard").type("xml").classifier("features")
                .version(asInProject());
    }

    @Before
    public void setUpITestBase() throws Exception {
        int count = 0;
        logger.info("Waited for Cassandra service to appear: " + Integer.toString(count * 500));

        session = sessionFactory.create(System.in, printStream, errStream);
    }

    protected String executeCommand(final String command) throws IOException {
        byteArrayOutputStream.flush();
        byteArrayOutputStream.reset();

        String response;
        FutureTask<String> commandFuture = new FutureTask<String>(new Callable<String>() {
            public String call() {
                try {
                    System.err.println(command);
                    session.execute(command);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                printStream.flush();
                errStream.flush();
                return byteArrayOutputStream.toString();
            }
        });

        try {
            executor.submit(commandFuture);
            response = commandFuture.get(10000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            response = "SHELL COMMAND TIMED OUT: ";
        }

        System.err.println(response);

        return response;
    }

    protected <T> T getOsgiService(Class<T> type, long timeout) {
        return getOsgiService(type, null, timeout);
    }

    protected <T> T getOsgiService(Class<T> type) {
        return getOsgiService(type, null, SERVICE_TIMEOUT);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected <T> T getOsgiService(Class<T> type, String filter, long timeout) {
        ServiceTracker tracker = null;
        try {
            String flt;
            if (filter != null) {
                if (filter.startsWith("(")) {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")" + filter + ")";
                } else {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")(" + filter + "))";
                }
            } else {
                flt = "(" + Constants.OBJECTCLASS + "=" + type.getName() + ")";
            }
            logger.info("---------, flt = " + flt);

            Filter osgiFilter = FrameworkUtil.createFilter(flt);
            tracker = new ServiceTracker(bc, osgiFilter, null);
            tracker.open(true);

            // Note that the tracker is not closed to keep the reference
            // This is buggy, as the service reference may change i think
            Object svc = type.cast(tracker.waitForService(timeout));
            if (svc == null) {
                Dictionary dic = bc.getBundle().getHeaders();
                logger.info("Test bundle headers: " + explode(dic));

                for (ServiceReference ref : asCollection(bc.getAllServiceReferences(null, null))) {
                    logger.info("ServiceReference: " + ref);
                }

                for (ServiceReference ref : asCollection(bc.getAllServiceReferences(null, flt))) {
                    logger.info("Filtered ServiceReference: " + ref);
                }

                logger.error("Gave up waiting for service " + flt);
                return null;
            }
            return type.cast(svc);
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException("Invalid filter", e);
        } catch (InterruptedException e) {
            logger.error("getOsgiService error!", e);
            throw new RuntimeException(e);
        }
    }

    /*
     * Explode the dictionary into a ,-delimited list of key=value pairs
     */
    @SuppressWarnings("rawtypes")
    private static String explode(Dictionary dictionary) {
        Enumeration keys = dictionary.keys();
        StringBuffer result = new StringBuffer();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            result.append(String.format("%s=%s", key, dictionary.get(key)));
            if (keys.hasMoreElements()) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    /**
     * Provides an iterable collection of references, even if the original array
     * is null
     */
    @SuppressWarnings("rawtypes")
    private static Collection<ServiceReference> asCollection(ServiceReference[] references) {
        return references != null ? Arrays.asList(references) : Collections.<ServiceReference>emptyList();
    }

}
