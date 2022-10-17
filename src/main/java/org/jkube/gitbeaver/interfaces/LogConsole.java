package org.jkube.gitbeaver.interfaces;

public interface LogConsole extends Logger {
    void command(String command);

    void ignore(String runtimeMessage);

    void success(String line);
}
