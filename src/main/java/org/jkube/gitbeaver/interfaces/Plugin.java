package org.jkube.gitbeaver.interfaces;

import java.util.List;

public interface Plugin {

    void init();

    List<Command> getCommands();

    void shutdown();
}
