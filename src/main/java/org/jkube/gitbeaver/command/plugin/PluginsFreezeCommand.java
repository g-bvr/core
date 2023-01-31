package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginManager;
import org.jkube.logging.Log;

import java.util.List;
import java.util.Map;

/**
 * Usage: plugins freeze
 */
public class PluginsFreezeCommand extends SimpleCommand {

    public PluginsFreezeCommand() {
        super("PLUGINS FREEZE", """
               This command freezes the plugin state. This means that subsequent calls of PLUGIN ENABLE, PLUGIN INSTALL
               or PLUGIN COMPILE are prohibited. This is considered a security feature and should be used in
               the core docker file setup before any custom beaver scripts are executed.
            """);
    }

    @Override
    protected void execute(WorkSpace workSpace, Map<String, String> arguments) {
        PluginManager.setPluginsFrozen();
    }

}
