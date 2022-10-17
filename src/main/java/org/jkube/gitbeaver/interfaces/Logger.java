package org.jkube.gitbeaver.interfaces;

public interface Logger {
    void log(String line);

    void warn(String runtimeMessage);

    void error(String line);
}
