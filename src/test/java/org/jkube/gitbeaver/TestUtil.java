package org.jkube.gitbeaver;

import org.jkube.application.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    private final static List<String> failures = new ArrayList<>();

    @BeforeAll
    public static void beforeTests() {
        Application.setFailureHandler((message, code) -> {
            System.err.println("Failure captured: "+message);
            failures.add(message);
        });
    }

    public static void beforeEachTest() {
        failures.clear();
    }


    public static void assertFailure() {
        Assertions.assertEquals(1, failures.size());
        failures.clear();
    }

    public static void assertNoFailures() {
        Assertions.assertTrue(failures.isEmpty());
    }

}
