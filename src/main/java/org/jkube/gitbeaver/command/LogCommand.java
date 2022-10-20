package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.logging.Log;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.jkube.logging.Log.onException;

/**
 * Usage: log ...
 */
public class LogCommand extends AbstractCommand {

    public LogCommand() {
        super(0, null, "log");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        GitBeaver.getApplicationLogger(variables).log(String.join(" ", arguments));
    }

}
