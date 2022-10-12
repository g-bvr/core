package org.jkube.gitbeaver.util.test;


import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.gitbeaver.util.FileUtil;

import java.nio.file.Path;
import java.util.Map;

public class GitBeaverAbstractTest {

    public static final String WORKDIR = "workdir";
    public static final String MAIN = "main";
    public static final String TEST_BVR = "test.bvr";

    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String EXPECTED = "expected";

    public static final String TARGET_CLASSES = "target/classes";

    public void runTest(Path path) {
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, TARGET_CLASSES);
        GitBeaver.run(Map.of(
                MAIN, INPUT+"/"+TEST_BVR,
                WORKDIR, path.toString()
        ));
        TestUtil.assertNoFailures();
        FileUtil.expectEqualTrees(path.resolve(OUTPUT), path.resolve(EXPECTED));
        FileUtil.empty(path.resolve(OUTPUT));
    }

}
