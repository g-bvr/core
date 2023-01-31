package org.jkube.gitbeaver.util;

import org.jkube.application.Application;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
                        Path destination = targetPath.resolve(sourcePath.relativize(source));
                        if (!destination.toFile().isDirectory()) {
                            onException(() -> Files.copy(source, destination)).fail("Could not copy " + source + " to " + destination);
                        }
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

    public static void expectEqualTrees(Path path1, Path path2) {
        Expect.equal(path1.toFile().exists(), path2.toFile().exists()).elseFail("mismatching file existence");
        if (!path1.toFile().exists()) {
            return;
        }
        File file1 = path1.toFile();
        File file2 = path2.toFile();
        Expect.equal(file1.isDirectory(), file2.isDirectory())
                .elseFail("Mismatching file type "+file1+" vs. "+file2);
        if (file1.isDirectory()) {
            String[] files1 = file1.list();
            if (files1 == null) {
                files1 = new String[0];
            }
            String[] files2 = file2.list();
            if (files2 == null) {
                files2 = new String[0];
            }
            Expect.equal(files1.length, files2.length);
            Arrays.sort(files1);
            Arrays.sort(files2);
            for (int i = 0; i < files1.length; i++) {
                Expect.equal(files1[i], files2[i]).elseFail("Mismatching files " + files1[i] + " vs. " + files2[i]);
                expectEqualTrees(path1.resolve(files1[i]), path2.resolve(files2[i]));
            }
        } else {
            List<String> lines1 = readLines(path1);
            List<String> lines2 = readLines(path2);
            Expect.equal(lines1.size(), lines2.size()).elseFail("Mismatching number of lines: "+path1+" ("+lines1.size()+") vs. "+path2+" ("+lines2.size()+")");
            for (int i = 0; i < lines1.size(); i++) {
                Expect.equal(lines1.get(i), lines2.get(i)).elseFail("Mismatching line #" + i +
                        " in file "+path1+ " vs. " + path2+": \n"+lines1.get(i)+"\n"+lines2.get(i));
            }
        }
    }

    public static List<String> readLines(Path path) {
        return onException(() -> Files.readAllLines(path)).fail("Could not load lines of "+path);
    }

    public static void append(String line, File file) {
        append(List.of(line), file);
    }

    public static void append(List<String> lines, File file) {
        try (PrintWriter output = new PrintWriter(new FileWriter(file, true))) {
            lines.forEach(output::println);
        } catch (IOException e) {
            Application.fail("could not append to file "+ file);
        }
    }

    public static void store(Path path, List<String> lines) {
        onException(() -> Files.write(path, lines)).fail("Could not write lines to file: "+path);
    }

    public static void storeWithoutNewline(Path path, String line) {
        onException(() -> Files.writeString(path, line)).fail("Could not write line to file: "+path);
    }

    public static void store(Path path, String text) {
        store(path, Collections.singletonList(text));
    }
}
