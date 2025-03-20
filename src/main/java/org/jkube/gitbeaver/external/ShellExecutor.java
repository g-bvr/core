package org.jkube.gitbeaver.external;

import org.jkube.gitbeaver.applicationlog.DefaultLogConsole;
import org.jkube.gitbeaver.util.ExternalProcess;

public class ShellExecutor {

    public boolean execute(String command) {
        ExternalProcess process = new ExternalProcess(null)
                .command(command.split(" "))
                .logConsole(new DefaultLogConsole())
                .execute();
        return process.hasFailed();
    }

}
