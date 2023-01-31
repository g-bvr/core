package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.external.ShellExecutor;
import org.jkube.logging.Log;

import java.util.List;
import java.util.Map;

/**
 * Usage: plugin enable fullClassName
 */
public class PluginInstallCommand extends SimpleCommand {

    private static final String PLUGIN_NAME = "plugin";

    private final ShellExecutor shellExecutor = new ShellExecutor();

    public PluginInstallCommand() {
        super("PLUGIN INSTALL", """
               This command executes system level installation steps required by a plugin.
               If there are any installation steps to be executed on system level is defined
               by the method Plugin.getCommands(). These steps are supposed to be executed
               in a shell script while setting up a docker container.
            """);
        argument(PLUGIN_NAME, """
                The name of the plugin for which system commands are to be executed.
            """);
    }

    @Override
    protected void execute(WorkSpace workSpace, Map<String, String> arguments) {
        if (GitBeaver.pluginManager().checkPluginsNotFrozen()) {
            List<String> script = GitBeaver.pluginManager().getPlugin(arguments.get(PLUGIN_NAME)).getInstallationScript();
            if (!script.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                Log.log("Running installation script with {} steps:", script.size());
                script.forEach(shellExecutor::execute);
            }
        }
    }

}
