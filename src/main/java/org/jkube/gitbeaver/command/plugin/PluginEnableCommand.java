package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Usage: plugin enable fullClassName
 */
public class PluginEnableCommand extends SimpleCommand {

    private static final String PLUGIN_NAME = "plugin";

    public PluginEnableCommand() {
        super("PLUGIN ENABLE", """
               This command enables a plugin. The class files must be present in the class path (e.g. as
               result of a PLUGIN COMPILE command executed before this).
            """);
        argument(PLUGIN_NAME, """
                The name of the plugin to be enabled.
            """);
    }

    @Override
    protected void execute(WorkSpace workSpace, Map<String, String> arguments) {
        if (!GitBeaver.pluginManager().enable(arguments.get(PLUGIN_NAME))) {
            Log.warn("Could not enable plugin (maybe it was enabled already?)");
        }
    }

}
