package com.mengka.springboot.karaf_02;

import com.mengka.microservices.karaf.service.MengkaService;
import com.mengka.microservices.karaf.service.values.MengkaReq;
import com.mengka.springboot.karaf_01.CalculatorTest;
import com.mengka.springboot.karaf_01.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.util.Dictionary;
import java.util.Hashtable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.OptionUtils.combine;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
@RunWith(PaxExam.class)
public class MengkaLimitServiceTest extends TestBase {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorTest.class);

    @Inject
    private ConfigurationAdmin configAdminService;

    @Configuration
    public Option[] config() {
        return options(
                combine(
                        configBase(),
                        mavenBundle()
                                .groupId("com.mengka.microservices.karaf")
                                .artifactId("mengka-services-api")
                                .version("1.0.1-SNAPSHOT"),
                        mavenBundle()
                                .groupId("com.mengka.microservices.karaf")
                                .artifactId("mengka-services-impl")
                                .version("1.0.1-SNAPSHOT")
                )
        );
    }

    @Test
    public void testLimitService() throws Exception {
        LOG.info("starting test");
        org.osgi.service.cm.Configuration configuration = configAdminService.createFactoryConfiguration("com.mengka.microservices.karaf.service");

        Dictionary<String, Object> dictionary = configuration.getProperties();
        if (dictionary == null) {
            dictionary = new Hashtable<String, Object>();
        }

        dictionary.put("institute", "JavaBank");
        dictionary.put("fee", "1500");
        LOG.info("updating configuration");

        configuration.setBundleLocation(null);
        configuration.update(dictionary);

        LOG.info("retrieving service");
        MengkaService osgiService = getOsgiService(MengkaService.class);

        assertThat(osgiService, is(notNullValue()));

        MengkaReq mengkaReq = new MengkaReq();
        mengkaReq.setCreditAmount(50000.0);
        mengkaReq.setApplyAmount(10000.0);
        LOG.info("-----------, mengkaReq = " + mengkaReq.toString());

        osgiService.calculateRate(mengkaReq);

        Double amount = mengkaReq.getAmount();
        LOG.info("----------, 信用额度 = " + amount);
    }
}
