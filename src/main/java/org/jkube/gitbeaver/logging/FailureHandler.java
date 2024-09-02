package org.jkube.gitbeaver.logging;

@FunctionalInterface
public interface FailureHandler {
	void fail(String message, int failureCode);
}
