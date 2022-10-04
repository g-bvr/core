package org.jkube.gitbeaver;


import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.util.Environment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class GitBeaverTest {

    public static final Map<String,String> VARIABLES = Map.of(
            "main", "helloworld.bvr",
            "workdir", "src/test/resources"
    );
    public static final String TARGET_CLASSES = "target/classes";

    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    public void helloWorldTest() {
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, TARGET_CLASSES);
        GitBeaver.run(VARIABLES);
        TestUtil.assertNoFailures();
    }

}
