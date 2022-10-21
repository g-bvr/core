package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.FileResolver;
import org.jkube.gitbeaver.util.FileUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.jkube.logging.Log.onException;

public class DefaultFileResolver implements FileResolver {

    @Override
    public List<String> resolve(Path workspacePath, Path filePath, Map<String, String> variables, boolean resolveVariables) {
        return FileUtil.readLines(filePath);
    }

}
