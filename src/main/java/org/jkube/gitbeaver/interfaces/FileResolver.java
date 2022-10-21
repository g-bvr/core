package org.jkube.gitbeaver.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface FileResolver {
    List<String> resolve(Path workspacePath, Path relativePath, Map<String, String> variables, boolean resolveVariables);

}
