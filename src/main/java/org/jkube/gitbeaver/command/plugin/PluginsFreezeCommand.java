package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginManager;
import org.jkube.logging.Log;

import java.util.List;

/**
 * Usage: plugins freeze
 */
public class PluginsFreezeCommand extends SimpleCommand {

    public PluginsFreezeCommand() {
        super(0, "plugins", "freeze");
    }

    @Override
    protected void execute(WorkSpace workSpace, List<String> arguments) {
        PluginManager.setPluginsFrozen();
    }

}
