package org.jkube.gitbeaver;

import org.jkube.gitbeaver.githandler.GitCloner;
import org.jkube.gitbeaver.util.FileUtil;
import org.jkube.gitbeaver.util.test.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class GitCloneTest {

    public static final String SRC_TEST_RESOURCES = "src/test/resources";
    public static final String WORKDIR = "workdir";
    public static final String PLUGIN = "plugin";
    private static final String GIT_URL = "https://github.com/kneissler";
    private static final String REPOSITORY = "git-beaver-base";

    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    void simulateCloneTest() {
        GitCloner cloner = new GitCloner();
        cloner.doSimulatedCloning(Path.of(SRC_TEST_RESOURCES));
        cloner.clone(Path.of(WORKDIR), null, PLUGIN, null);
        TestUtil.assertNoFailures();
        Assertions.assertTrue(new File(WORKDIR+"/"+PLUGIN+"/org").exists());
        FileUtil.delete(Path.of(WORKDIR + "/" + PLUGIN));
    }

    @Test
    void realCloneTest() throws MalformedURLException {
        GitCloner cloner = new GitCloner();
        cloner.clone(Path.of(WORKDIR), new URL(GIT_URL), REPOSITORY, null);
        TestUtil.assertNoFailures();
        Assertions.assertTrue(new File(WORKDIR+"/"+REPOSITORY+"/pom.xml").exists());
        FileUtil.delete(Path.of(WORKDIR + "/" + REPOSITORY));
    }

    @Test
    void realCloneTagTest() throws MalformedURLException {
        GitCloner cloner = new GitCloner();
        cloner.clone(Path.of(WORKDIR), new URL(GIT_URL), REPOSITORY, "main");
        TestUtil.assertNoFailures();
        Assertions.assertTrue(new File(WORKDIR+"/"+REPOSITORY+"/pom.xml").exists());
        FileUtil.delete(Path.of(WORKDIR + "/" + REPOSITORY));
    }

}
