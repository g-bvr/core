package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.FileResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.jkube.logging.Log.onException;

public class DefaultFileResolver implements FileResolver {
    @Override
    public List<String> resolve(Path workspacePath, Path filePath, Map<String, String> variables) {
        return onException(() -> Files.readAllLines(filePath)).fail("Could not load lines of file: "+filePath);
    }
}
