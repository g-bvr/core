package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;

import java.nio.file.Path;
import java.util.List;

/**
 * Usage: plugin enable fullClassName
 */
public class PluginEnableCommand extends SimpleCommand {

    public PluginEnableCommand() {
        super(1, "plugin", "enable");
    }

    @Override
    protected void execute(WorkSpace workSpace, List<String> arguments) {
        GitBeaver.pluginManager().enable(arguments.get(0));
    }

}
