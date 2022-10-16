package org.jkube.gitbeaver.plugin;

import org.jkube.logging.Log;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jkube.logging.Log.exception;
import static org.jkube.logging.Log.onException;

public class PluginCompiler {

    private static final String JAVA_EXTENSION = ".java";
    private final Path targetPath;

    private final JavaCompiler compiler;

    public PluginCompiler(Path targetPath) {
        this.targetPath = targetPath.toAbsolutePath();
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    public void compile(Path sourcePath) {
        if (PluginManager.checkPluginsNotFrozen()) {
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
        compiler.run(null, null, null, args.toArray(new String[0]));
        Log.log("... done ({}ms)", System.currentTimeMillis()-timestamp);
    }


}
