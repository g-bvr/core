package org.jkube.gitbeaver.external;

import org.jkube.gitbeaver.DefaultLogConsole;
import org.jkube.gitbeaver.util.ExternalProcess;
import org.jkube.gitbeaver.util.FileUtil;

import java.net.URL;
import java.nio.file.Path;

public class ShellExecutor {

    public boolean execute(String command) {
        ExternalProcess process = new ExternalProcess()
                .command(command)
                .logConsole(new DefaultLogConsole())
                .execute();
        return process.hasFailed();
    }

}
