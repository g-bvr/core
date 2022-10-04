package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
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
                walk.filter(Files::isRegularFile)
                        .filter(this::isJavaFile)
                        .forEach(this::compileFile);
            } catch (IOException e) {
                Log.exception(e);
            }
        }
    }

    private boolean isJavaFile(Path path) {
        return path.toString().endsWith(JAVA_EXTENSION);
    }

    private void compileFile(Path path) {
        Log.log("Compiling "+path);
        compiler.run(null, null, null, "-d", targetPath.toString(), path.toString());
    }


}
