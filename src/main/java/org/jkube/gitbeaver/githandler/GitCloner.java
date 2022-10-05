package org.jkube.gitbeaver.githandler;

import org.jkube.gitbeaver.DefaultLogConsole;
import org.jkube.gitbeaver.util.ExternalProcess;
import org.jkube.gitbeaver.util.FileUtil;

import java.net.URL;
import java.nio.file.Path;

public class GitCloner {

    private Path simulateClonePath = null;

    public void doSimulatedCloning(Path repositoryPath) {
        simulateClonePath = repositoryPath;
    }

    public void clone(Path workdir, URL gitUrl, String repository, String tag) {
        if (simulateClonePath != null) {
            Path sourcePath = createSimulatedGitPath(repository, tag);
            Path targetPath = workdir.resolve(repository);
            FileUtil.createIfNotExists(targetPath);
            FileUtil.copyTree(sourcePath, targetPath);
        } else {
            ExternalProcess clone = new ExternalProcess();
            String repoUrl = gitUrl.toString()+"/"+repository;
            if (tag == null) {
                clone.command("git", "clone", "-c", "advice.detachedHead=false", "-b", tag, repoUrl);
            } else {
                clone.command("git", "clone", repoUrl);
            }
            clone.dir(workdir).logConsole(new DefaultLogConsole()).execute();
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
