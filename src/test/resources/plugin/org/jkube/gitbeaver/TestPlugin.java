package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.plugin.SimplePlugin;
import org.jkube.gitbeaver.logging.Log;

import java.util.List;

public class TestPlugin extends SimplePlugin {

    public TestPlugin() {
        super("Dummy plugin for testing");
    }

    @Override
    public List<Command> getCommands() {
        return List.of(new TestCommand());
    }

    @Override
    public void init() {
        Log.log("The test plugin was initialized");
    }

    @Override
    public void shutdown() {
        Log.log("The test plugin was shut down");
    }
}
