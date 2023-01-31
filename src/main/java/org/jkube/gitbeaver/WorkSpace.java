package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.FileUtil;
import org.jkube.util.Expect;

import java.nio.file.Path;

import static org.jkube.logging.Log.onException;

public class WorkSpace {

    private final Path workdir;

    public WorkSpace(String workdir) {
        this(Path.of(workdir));
    }

    public WorkSpace(Path workdirPath) {
        this.workdir = onException(() -> workdirPath.toRealPath()).fail("could not get real path: "+workdirPath);
    }

    public Path getWorkdir() {
        return workdir;
    }

    public Path getAbsolutePath(String relativePath) {
        if (relativePath == null) {
            return workdir;
        }
        Path resolvedPath = workdir.resolve(relativePath);
        Path parentPath = resolvedPath.getParent();
        Path child = resolvedPath.getFileName();
        FileUtil.createIfNotExists(parentPath);
        Path parentRealPath = onException(() -> parentPath.toRealPath())
                .fail("could not get parent's real path: "+resolvedPath);
        Expect.isTrue(parentRealPath.startsWith(workdir))
                .elseFail("Resolved path's parent is outside workspace: "+parentPath);
        return parentRealPath.resolve(child);
    }

    public Path getRelativePath(Path absolutePath) {
        Path realPath = onException(() -> absolutePath.toRealPath())
                .fail("could not get real path: "+absolutePath);
        Expect.isTrue(realPath.startsWith(workdir))
                .elseFail("Path is outside workspace: "+realPath);
        return workdir.relativize(absolutePath);

    }

    public WorkSpace getSubWorkspace(String relativePath) {
        return new WorkSpace(getAbsolutePath(relativePath));
    }
}
