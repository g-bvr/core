package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.external.ShellExecutor;
import org.jkube.logging.Log;

import java.util.List;

/**
 * Usage: plugin enable fullClassName
 */
public class PluginInstallCommand extends SimpleCommand {

    private final ShellExecutor shellExecutor = new ShellExecutor();

    public PluginInstallCommand() {
        super(1, "plugin", "install");
    }

    @Override
    protected void execute(WorkSpace workSpace, List<String> arguments) {
         List<String> script = GitBeaver.pluginManager().getPlugin(arguments.get(0)).getInstallationScript();
         if (!script.isEmpty()) {
             StringBuilder sb = new StringBuilder();
             Log.log("Running installation script with {} steps:", script.size());
             script.forEach(shellExecutor::execute);
         }
    }

}
