package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;

import java.util.List;
import java.util.Map;

public abstract class SimpleCommand extends AbstractCommand {

    protected SimpleCommand(int numArgs, String... keywords) {
        super(numArgs, numArgs, keywords);
    }

    protected abstract void execute(WorkSpace workSpace, List<String> arguments);

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        execute(workSpace, arguments);
    }
}
