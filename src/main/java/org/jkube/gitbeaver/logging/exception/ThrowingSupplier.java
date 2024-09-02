package org.jkube.gitbeaver.logging.exception;

@FunctionalInterface
public interface ThrowingSupplier<R> {
	R get() throws Throwable;
}
