package org.jkube.gitbeaver.util.test;

import org.jkube.application.Application;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    private final static List<String> failures = new ArrayList<>();

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
        if (failures.size() != 1)
            throw new RuntimeException("expected exactly one failure, got: "+failures.size());
        failures.clear();
    }

    public static void assertNoFailures() {
        if (failures.size() != 0)
            throw new RuntimeException("there were unexpected failures");
    }

}
