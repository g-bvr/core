package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.interfaces.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jkube.gitbeaver.logging.Log.onException;

public class SimplePlugin implements Plugin {

    private final String desription;
    private final List<Command> commands;

    @SafeVarargs
    public SimplePlugin(String desciption, Class<? extends Command>... commandClasses) {
        this.desription = desciption;
        this.commands = new ArrayList<>();
        for (Class<? extends Command> c : commandClasses) {
            this.commands.add(instantiate(c));
        }
    }

    public String getDescription() {
        return desription;
    }

    private Command instantiate(Class<? extends Command> commandClass) {
        return onException(() -> commandClass.getConstructor().newInstance()).fail("Could not instantiate class "+commandClass);
    }

    @Override
    public List<String> getInstallationScript() {
        return Collections.emptyList();
    }

    @Override
    public void init() {
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void shutdown() {
    }
}
