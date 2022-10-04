package org.jkube.gitbeaver.util;

import org.jkube.application.Application;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.jkube.logging.Log.onException;

public class FileUtil {

    public static void clear(Path directory) {
        clearRecursively(directory.toFile(), false);
    }

    public static void delete(Path directory) {
        clearRecursively(directory.toFile(), true);
    }

    private static void clearRecursively(File file, boolean deleteDir) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File sub : files) {
                    clearRecursively(sub, true);
                }
            }
        }
        if (deleteDir) {
            onException(() -> Files.delete(file.toPath())).fail("Could not delete file " + file);
        }
    }

    public static void copyTree(Path sourcePath, Path targetPath) {
        try(Stream<Path> files = Files.walk(sourcePath)) {
                files.forEach(source -> {
                Path destination = Paths.get(String.valueOf(targetPath), source.toString().substring(sourcePath.toString().length()));
                onException(() -> Files.copy(source, destination)).fail("Could not copy " + source + " to " + destination);
            });
        } catch (IOException e) {
            Application.fail("Could not walk files in "+sourcePath);
        }
    }

    public static void createIfNotExists(Path path) {
        if (!path.toFile().exists()) {
            Log.log("Creating target directory "+path);
            Expect.isTrue(path.toFile().mkdirs())
                    .elseFail("Could not create target directory "+path);
        }
    }
}
