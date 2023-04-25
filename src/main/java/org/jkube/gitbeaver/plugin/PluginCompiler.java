package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.logging.Log;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginCompiler {

    private static final String JAVA_EXTENSION = ".java";
    private final Path targetPath;

    private final JavaCompiler compiler;
    private final List<File> jarFiles;

    public PluginCompiler(Path targetPath) {
        this(targetPath, Collections.emptyList());
    }

    public PluginCompiler(Path targetPath, List<File> jarFiles) {
        this.targetPath = targetPath.toAbsolutePath();
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.jarFiles = jarFiles;
    }

    public void compile(Path sourcePath) {
        if (GitBeaver.pluginManager().checkPluginsNotFrozen()) {
            try (Stream<Path> walk = Files.walk(sourcePath)) {
                compileFiles(walk.filter(Files::isRegularFile)
                        .filter(this::isJavaFile)
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                Log.exception(e);
            }
        }
    }

    private boolean isJavaFile(Path path) {
        return path.toString().endsWith(JAVA_EXTENSION);
    }

    private void compileFiles(List<Path> javaFiles) {
        long timestamp = System.currentTimeMillis();
        Log.log("Compiling {} java files...", javaFiles.size());
        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add(targetPath.toString());
        javaFiles.forEach(jf -> args.add(jf.toString()));
        // set compiler's classpath to be same as the runtime's plus additional jar files
        StringBuilder classPath = new StringBuilder();
        classPath.append(System.getProperty("java.class.path"));
        jarFiles.forEach(jf -> classPath.append(":").append(jf));
        args.add("-classpath");
        args.add(classPath.toString());
        compiler.run(null, null, null, args.toArray(new String[0]));
        Log.log("... done ({}ms)", System.currentTimeMillis()-timestamp);
    }


}
