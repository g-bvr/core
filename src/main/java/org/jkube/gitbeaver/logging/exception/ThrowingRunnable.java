package org.jkube.gitbeaver.logging.exception;

@FunctionalInterface
public interface ThrowingRunnable {
	void run() throws Throwable;
}
