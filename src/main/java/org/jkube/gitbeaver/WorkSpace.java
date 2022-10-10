package org.jkube.gitbeaver;

import org.jkube.util.Expect;

import java.nio.file.Path;

import static org.jkube.logging.Log.onException;

public class WorkSpace {

    private final Path workdir;

    public WorkSpace(String workdir) {
        this.workdir = onException(() -> Path.of(workdir).toRealPath()).fail("could not get real path: "+workdir);
    }

    public Path getWorkdir() {
        return workdir;
    }

    public Path getAbsolutePath(String relativePath) {
        Path resolvedPath = workdir.resolve(relativePath);
        Path parentPath = resolvedPath.getParent();
        Path child = resolvedPath.getFileName();
        Path parentRealPath = onException(() -> parentPath.toRealPath())
                .fail("could not get parent's real path: "+resolvedPath);
        Expect.isTrue(parentRealPath.startsWith(workdir))
                .elseFail("Resolved path's parent is outside workspace: "+parentPath);
        return parentRealPath.resolve(child);
    }

}
