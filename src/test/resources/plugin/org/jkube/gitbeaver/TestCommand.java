package org.jkube.gitbeaver;

import org.jkube.logging.Log;

import java.util.Map;

public class TestCommand extends SimpleCommand {
    protected TestCommand() {
        super("test", "test");
    }

    @Override
    protected void execute(WorkSpace workSpace, Map<String, String> arguments) {
        Log.log("Executed test command");
    }
}
