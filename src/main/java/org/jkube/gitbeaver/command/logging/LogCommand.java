package org.jkube.gitbeaver.command.logging;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;

/**
 * Usage: log ...
 */
public class LogCommand extends AbstractCommand {

    public LogCommand() {
        super("Log a text");
        commandlineVariant("LOG *", "Log a text");
        commandlineVariant("LOG", "Log an empty line");
        argument(REST, "String to be logged");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        GitBeaver.getApplicationLogger(variables).log(arguments.get(REST));
    }

}
