package org.jkube.gitbeaver.interfaces;

public interface LogConsole {
    void command(String command);

    void ignore(String runtimeMessage);

    void warn(String runtimeMessage);

    void success(String line);

    void error(String line);

    void add(String line);
}
