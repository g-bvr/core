package org.jkube.gitbeaver.command;

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
public class LogCommand implements Command {

    @Override
    public int minNumArgs() {
        return 0;
    }

    @Override
    public Integer maxNumArgs() {
        return null;
    }

    @Override
    public List<String> keywords() {
        return List.of("log");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        Log.log(String.join(" ", arguments));
    }

}
