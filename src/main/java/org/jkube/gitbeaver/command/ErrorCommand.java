package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.List;
import java.util.Map;

/**
 * Usage: log ...
 */
public class ErrorCommand extends AbstractCommand {

    public ErrorCommand() {
        super(0, null, "error");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        GitBeaver.getApplicationLogger(variables).error(String.join(" ", arguments));
    }

}
