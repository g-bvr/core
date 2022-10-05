package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.LogConsole;
import org.jkube.logging.Log;

public class DefaultLogConsole implements LogConsole {
    @Override
    public void command(String message) {
        Log.log(message);
    }

    @Override
    public void ignore(String message) {
        Log.log(message);
    }

    @Override
    public void warn(String message) {
        Log.warn(message);
    }

    @Override
    public void success(String message) {
        Log.log(message);
    }

    @Override
    public void error(String message) {
        Log.error(message);
    }

    @Override
    public void add(String message) {
        Log.log(message);
    }
}
