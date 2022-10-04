package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;

import java.util.List;
import java.util.Map;

public abstract class SimpleCommand implements Command {

    private final int numArgs;
    private final List<String> keywords;

    protected SimpleCommand(int numArgs, String... keywords) {
        this.numArgs = numArgs;
        this.keywords = List.of(keywords);
    }

    protected abstract void execute(WorkSpace workSpace, List<String> arguments);

    @Override
    public int minNumArgs() {
        return numArgs;
    }

    @Override
    public Integer maxNumArgs() {
        return numArgs;
    }

    @Override
    public List<String> keywords() {
        return keywords;
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        execute(workSpace, arguments);
    }
}
