package org.jkube.gitbeaver.interfaces;

@FunctionalInterface
public interface ApplicationLoggerFactory {

    ApplicationLogger createLogger(String runIdentifier);

}
