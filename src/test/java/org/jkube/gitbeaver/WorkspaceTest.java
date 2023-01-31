package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.test.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class WorkspaceTest {

    public static final String SRC_TEST_RESOURCES = "src/test/resources";
    public static final String WORKDIR = "workdir";

    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    void testPathInside() {
        WorkSpace w = new WorkSpace(SRC_TEST_RESOURCES);
        Path p = w.getAbsolutePath(WORKDIR);
        System.out.println("Workspace: "+w.getWorkdir());
        System.out.println("Path: "+p);
        Assertions.assertTrue(p.startsWith(w.getWorkdir()));
        TestUtil.assertNoFailures();
    }

    @Test
    void testPathOutside() {
        WorkSpace w = new WorkSpace(SRC_TEST_RESOURCES);
        Path p = w.getAbsolutePath("../xxx");
        TestUtil.assertFailure();
    }

}
