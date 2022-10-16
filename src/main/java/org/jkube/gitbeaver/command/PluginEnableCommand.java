package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

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
        if (!GitBeaver.pluginManager().enable(arguments.get(0))) {
            Log.warn("Could not enable plugin (maye it was enabled already)");
        }
    }

}
