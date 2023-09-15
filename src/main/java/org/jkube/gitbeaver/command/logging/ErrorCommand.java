package org.jkube.gitbeaver.command.logging;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;

/**
 * Usage: log ...
 */
public class ErrorCommand extends AbstractCommand {

    public ErrorCommand() {
        super("Log a text as error");
        commandlineVariant("ERROR *", "Log a text as error");
        argument(REST, "String to be logged");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        GitBeaver.getApplicationLogger(variables).error(arguments.get(REST));
        throw new RuntimeException("An error occured");
    }

}
