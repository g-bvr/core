package org.jkube.gitbeaver.application;

@FunctionalInterface
public interface FailureHandler {
	void fail(String message, int failureCode);
}