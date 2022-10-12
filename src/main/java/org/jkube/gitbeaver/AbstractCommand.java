package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.util.Expect;

import java.util.List;
import java.util.Map;

public abstract class AbstractCommand implements Command {

    private final int minNumArgs;
    private final Integer maxNumArgs;
    private final List<String> keywords;

    protected AbstractCommand(int minNumArgs, Integer maxNumArgs, String... keywords) {
        this.minNumArgs = minNumArgs;
        this.maxNumArgs = maxNumArgs;
        this.keywords = List.of(keywords);
    }

    @Override
    public int minNumArgs() {
        return minNumArgs;
    }

    @Override
    public Integer maxNumArgs() {
        return maxNumArgs;
    }

    @Override
    public List<String> keywords() {
        return keywords;
    }

    protected void expectArg(int pos, String expected, List<String> arguments) {
        Expect.isTrue(expected.equalsIgnoreCase(arguments.get(pos))).elseFail("Expected '"+expected+"' at position "+pos+", got: "+arguments.get(pos));
    }

}
