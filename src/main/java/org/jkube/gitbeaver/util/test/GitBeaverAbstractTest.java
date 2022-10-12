package org.jkube.gitbeaver.util.test;


import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.gitbeaver.util.FileUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GitBeaverAbstractTest {

    public static final String WORKDIR = "workdir";
    public static final String MAIN = "main";
    public static final String TEST_BVR = "test"+ GitBeaver.BEAVER_EXTENSION;

    public static final String OUTPUT = "output";
    public static final String EXPECTED = "expected";

    public static final String TARGET_CLASSES = "target/classes";

    public void runTest(Path path) {
        FileUtil.clear(path.resolve(OUTPUT));
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, TARGET_CLASSES);
        GitBeaver.run(new HashMap<>(Map.of(
                MAIN, TEST_BVR,
                WORKDIR, path.toString()
        )));
        TestUtil.assertNoFailures();
        FileUtil.expectEqualTrees(path.resolve(EXPECTED), path.resolve(OUTPUT));
        FileUtil.clear(path.resolve(OUTPUT));
    }

}
