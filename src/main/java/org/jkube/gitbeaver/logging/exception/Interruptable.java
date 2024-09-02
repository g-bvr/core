package org.jkube.gitbeaver.logging.exception;

@FunctionalInterface
public interface Interruptable {
	void run() throws InterruptedException;
}
