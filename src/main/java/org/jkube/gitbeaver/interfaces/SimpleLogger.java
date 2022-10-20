package org.jkube.gitbeaver.interfaces;

public interface SimpleLogger {
    void log(String line);

    void warn(String line);

    void error(String line);
}
