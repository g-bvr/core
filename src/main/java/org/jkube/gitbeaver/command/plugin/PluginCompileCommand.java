package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;
import org.jkube.gitbeaver.util.Environment;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Usage: plugin compile soureDir
 */
public class PluginCompileCommand extends SimpleCommand {

    private static final String PLUGIN_DIRECTORY = "directory";
    private static final String LIBRARIES_FOLDER = "lib";
    private static final String JAR_EXTENSION = ".jar";

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
        File[] jarFiles = sourcePath
                .resolve(LIBRARIES_FOLDER)
                .toFile()
                .listFiles((d,fn) -> fn .endsWith(JAR_EXTENSION));
        new PluginCompiler(Environment.classPath(), jarFiles == null ? Collections.emptyList() : List.of(jarFiles)).compile(sourcePath);
    }

}
