package com.mengka.microservices.karaf.command.completer;

import com.mengka.microservices.karaf.service.MengkaService;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.ArgumentCommandLine;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import java.util.List;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
@Service
public class MengkaServiceCompleter implements Completer {

    @Reference
    private List<MengkaService> mengkaServices;

    StringsCompleter delegate = new StringsCompleter(false);

    public int complete(Session session, CommandLine commandLine, List<String> candidates) {
        if (session != null) {
            if (commandLine instanceof ArgumentCommandLine) {
                delegate.getStrings().add(commandLine.getCursorArgument());
            } else {
                for (MengkaService creditCalculator : mengkaServices) {
                    delegate.getStrings().add(creditCalculator.getInstitute());
                }
            }
        }
        return delegate.complete(session, commandLine, candidates);
    }
}
