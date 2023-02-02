package org.jkube.gitbeaver.interfaces;

import java.util.List;

public interface Plugin {

    List<String> getInstallationScript();

    void init();
    String getDescription();

    List<Command> getCommands();

    void shutdown();
}
