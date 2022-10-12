package org.jkube.gitbeaver;


import org.jkube.gitbeaver.util.test.GitBeaverAbstractTest;
import org.jkube.gitbeaver.util.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class HelloWorldTest extends GitBeaverAbstractTest {

    private static final String TEST_PATH = "src/test/resources/helloworld";

    @BeforeAll
    static void beforeAllTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTest() { TestUtil.beforeEachTest(); }

    @Test
    void helloWorldTest() {
        runTest(Path.of(TEST_PATH));
    }
    @AfterEach
    void afterEachTest() { TestUtil.assertNoFailures(); }


}
