package com.mengka.microservices.karaf.command;

import com.mengka.microservices.karaf.command.completer.MengkaServiceCompleter;
import com.mengka.microservices.karaf.service.MengkaService;
import com.mengka.microservices.karaf.values.MengkaReq;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import java.util.List;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
@Command(scope = "microservice", name = "Mengka", description = "Calculates a Credit based on the service")
@Service
public class MengkaAction implements Action {

    @Reference
    private List<MengkaService> mengkaServices;

    @Argument(name = "argument", description = "Argument to the command", required = false, multiValued = false)
    @Completion(MengkaServiceCompleter.class)
    private String argument;

    public Object execute() throws Exception {
        MengkaService service = null;

        for (MengkaService creditCalculator : mengkaServices) {
            if (creditCalculator.getInstitute().equalsIgnoreCase(argument)) {
                service = creditCalculator;
                break;
            }
        }

        if (service == null) {
            return "No service found with institue name: "+argument;
        }

        MengkaReq creditValues = new MengkaReq();
        creditValues.setApplyAmount(10000.0);
        creditValues.setCreditAmount(50000.0);

        service.calculateRate(creditValues);

        return creditValues.getAmount();
    }
}
