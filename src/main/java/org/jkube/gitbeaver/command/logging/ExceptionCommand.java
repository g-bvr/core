package org.jkube.gitbeaver.command.logging;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;

public class ExceptionCommand extends AbstractCommand {

    public ExceptionCommand() {
        super("Throws a runtime exception");
        commandlineVariant("EXCEPTION *", "Throws an exception");
        argument(REST, "String to be logged to error console and used as exception message");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        String text = arguments.get(REST);
        GitBeaver.getApplicationLogger(variables).error(text);
        throw new RuntimeException("EXCEPTION: "+text);
    }

}
