package org.jkube.gitbeaver;

import org.jkube.logging.Log;

import java.util.List;

public class TestCommand extends SimpleCommand {

    protected TestCommand() {
        super(0, "test");
    }

    @Override
    protected void execute(WorkSpace workSpace, List<String> arguments) {
        Log.log("Executed test command");
    }
}
