package org.jkube.gitbeaver.command.logging;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.util.List;
import java.util.Map;

/**
 * Usage: log ...
 */
public class WarnCommand extends AbstractCommand {

    public WarnCommand() {
        super(0, null, "warn");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        GitBeaver.getApplicationLogger(variables).warn(String.join(" ", arguments));
    }

}
