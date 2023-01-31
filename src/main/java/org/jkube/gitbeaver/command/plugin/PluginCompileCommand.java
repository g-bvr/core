package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;
import org.jkube.gitbeaver.util.Environment;

import java.nio.file.Path;
import java.util.Map;

/**
 * Usage: plugin compile soureDir
 */
public class PluginCompileCommand extends SimpleCommand {

    private static final String PLUGIN_DIRECTORY = "directory";

    public PluginCompileCommand() {
        super("PLUGIN COMPILE", """
               This command compiles the source files of a plugin. The java sources are supposed to reside in a
               directory within the workspace. The compiled class files are written to the class path.
            """);
        argument(PLUGIN_DIRECTORY, """
                The directory (relative to workspace path) in which the plugin source files are located.
            """);
    }

    @Override
    protected void execute(WorkSpace workSpace, Map<String, String> arguments) {
        Path sourcePath = workSpace.getAbsolutePath(arguments.get(PLUGIN_DIRECTORY));
        new PluginCompiler(Environment.classPath()).compile(sourcePath);
    }

}
