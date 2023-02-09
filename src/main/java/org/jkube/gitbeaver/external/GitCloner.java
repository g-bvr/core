package org.jkube.gitbeaver.external;

import org.jkube.gitbeaver.applicationlog.DefaultLogConsole;
import org.jkube.gitbeaver.interfaces.ApplicationLogger;
import org.jkube.gitbeaver.util.ExternalProcess;
import org.jkube.gitbeaver.util.FileUtil;

import java.net.URL;
import java.nio.file.Path;

public class GitCloner {

    private Path simulateClonePath = null;

    public void doSimulatedCloning(Path repositoryPath) {
        simulateClonePath = repositoryPath;
    }

    public void doSimulatedCloning(String repositoryPathOrNull) {
        simulateClonePath = repositoryPathOrNull == null ? null : Path.of(repositoryPathOrNull);
    }

    public void clone(Path workdir, URL gitUrl, String repository, String tag, ApplicationLogger logger) {
        clone(workdir, gitUrl, gitUrl, repository, tag, logger);
    }

    public void clone(Path workdir, URL gitUrl, URL maskedGitUrl, String repository, String tag, ApplicationLogger logger) {
        if (simulateClonePath != null) {
            Path sourcePath = createSimulatedGitPath(repository, tag);
            Path targetPath = workdir.resolve(repository);
            FileUtil.createIfNotExists(targetPath.getParent());
            FileUtil.copyTree(sourcePath, targetPath);
        } else {
            ExternalProcess clone = new ExternalProcess();
            String url = gitUrl+"/"+repository;
            String maskedUrl = maskedGitUrl+"/"+repository;
            if (tag == null) {
                clone
                        .command("git", "clone", url)
                        .loggedCommand("git", "clone", maskedUrl);
            } else {
                clone
                        .command("git", "clone", "-c", "advice.detachedHead=false", "-b", tag, url)
                        .loggedCommand("git", "clone", "-c", "advice.detachedHead=false", "-b", tag, maskedUrl);
            }
            clone
                .dir(workdir)
                .successMarker("Cloning into ")
                .logConsole(logger.createSubConsole())
                .execute();
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
