package org.jkube.gitbeaver.githandler;

import org.jkube.gitbeaver.util.FileUtil;

import java.net.URL;
import java.nio.file.Path;

public class GitCloner {

    private Path simulateClonePath = null;

    public void doSimulatedCloning(Path repositoryPath) {
        simulateClonePath = repositoryPath;
    }

    public void clone(Path workdir, URL gitUrl, String repository, String tag) {
        if (simulateClonePath == null) {
            Path sourcePath = createSimulatedGitPath(repository, tag);
            Path targetPath = workdir.resolve(repository);
            FileUtil.createIfNotExists(targetPath);
            FileUtil.copyTree(sourcePath, targetPath);
        } else {
            // TODO
            throw new RuntimeException("not yet implemented");
        }
    }

    private Path createSimulatedGitPath(String repository, String tag) {
        Path res = simulateClonePath.resolve(repository);
        if (tag != null) {
            res = res.resolve(tag);
        }
        return res;
    }

}
