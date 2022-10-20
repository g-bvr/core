package org.jkube.gitbeaver.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CombinedLogConsole implements LogConsole {

    private final List<LogConsole> consoles;
    public CombinedLogConsole(Stream<LogConsole> logConsoleStream) {
        consoles = new ArrayList<>();
        logConsoleStream.forEach(consoles::add);
    }

    @Override
    public void command(String line) {
        consoles.forEach(c -> c.command(line));
    }

    @Override
    public void ignore(String line) {
        consoles.forEach(c -> c.ignore(line));
    }

    @Override
    public void success(String line) {
        consoles.forEach(c -> c.success(line));
    }

    @Override
    public void log(String line) {
        consoles.forEach(c -> c.log(line));
    }

    @Override
    public void warn(String line) {
        consoles.forEach(c -> c.warn(line));
    }

    @Override
    public void error(String line) {
        consoles.forEach(c -> c.error(line));
    }
}
