package org.jkube.gitbeaver.command.logging;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;

public class ErrorCommand extends AbstractCommand {

    public ErrorCommand() {
        super("Log a an error and exit the application with error code");
        commandlineVariant("ERROR *", "Log a text as error and exit application");
        argument(REST, "String to be logged");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        String error = arguments.get(REST);
        GitBeaver.getApplicationLogger(variables).error(error);
        GitBeaver.getApplicationLogger(variables).warn("Terminating application with exit code 1 due to error");
        System.exit(1);
    }

}
