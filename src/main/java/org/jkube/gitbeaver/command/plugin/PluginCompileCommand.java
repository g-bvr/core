package org.jkube.gitbeaver.command.plugin;

import org.jkube.gitbeaver.AbstractCommand;
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
public class PluginCompileCommand extends AbstractCommand {

    private static final String SOURCES_DIRECTORY = "sources";
    private static final String JAR_DIRECTORY = "jars";
    private static final String JAR_EXTENSION = ".jar";

    public PluginCompileCommand() {
        super("""
               This command compiles the source files of a plugin. The java sources are supposed to reside in a
               directory within the workspace. The compiled class files are written to the class path.
            """);
        commandlineVariant("PLUGIN COMPILE "+SOURCES_DIRECTORY, "Compile without libraries");
        commandlineVariant("PLUGIN COMPILE "+SOURCES_DIRECTORY+" "+JAR_DIRECTORY, "Compile sources using libraries in jar files");
        argument(SOURCES_DIRECTORY, """
                The directory (relative to workspace path) in which the plugin source files are located.
            """);
        argument(JAR_DIRECTORY, """
                The directory (relative to workspace path) in which the library jar files files are located.
            """);
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        Path sourcePath = workSpace.getAbsolutePath(arguments.get(SOURCES_DIRECTORY));
        Path jarPath = workSpace.getAbsolutePath(arguments.get(JAR_DIRECTORY));
        File[] jarFiles = jarPath == null ? null :
                sourcePath
                .resolve(jarPath)
                .toFile()
                .listFiles((d,fn) -> fn .endsWith(JAR_EXTENSION));
        new PluginCompiler(Environment.classPath(), jarFiles == null ? Collections.emptyList() : List.of(jarFiles)).compile(sourcePath);
    }

}
