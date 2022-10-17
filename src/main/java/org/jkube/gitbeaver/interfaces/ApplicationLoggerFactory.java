package org.jkube.gitbeaver.interfaces;

import java.util.List;

@FunctionalInterface
public interface ApplicationLoggerFactory {

    ApplicationLogger createLogger(String runIdentifier);

}
