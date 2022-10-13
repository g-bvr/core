package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {

    private final CommandParser commandParser;

    public ScriptExecutor(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public void execute(String script, String forItem, Map<String, String> variables, WorkSpace workSpace) {
        Log.log("Executing script "+script+(forItem == null ? "" : " for "+forItem)+" in "+workSpace.getWorkdir());
        List<String> scriptLines = GitBeaver.fileResolver().resolve(
                workSpace.getWorkdir(),
                workSpace.getAbsolutePath(script),
                variables);
        Expect.notNull(scriptLines).elseFail("Script file does not exist: "+script);
        scriptLines.forEach(line -> executeLine(line, variables, workSpace));
    }

    private void executeLine(String line, Map<String, String> variables, WorkSpace workSpace) {
        List<String> arguments = new ArrayList<>();
        String substituted = DefaultFileResolver.substituteVariables(line, variables);
        Command command = commandParser.parseCommand(substituted, arguments);
        if (command != null) {
            command.execute(variables, workSpace, arguments);
        }
    }
}
