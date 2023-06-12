package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.util.VariableSubstitution;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {

    public static final String RETURN_VALUE_VARIABLE = "RETURN_VALUE";

    private final CommandParser commandParser;

    private String currentLine;
    private String previousLine;
    private String currentSubstitutedLine;
    private String previousSubstitutedLine;

    public ScriptExecutor(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public String execute(String script, String forItem, Map<String, String> variables, WorkSpace workSpace) {
        return execute(script, forItem, variables, workSpace, workSpace);
    }

    public String execute(String script, String forItem, Map<String, String> variables, WorkSpace scriptWorkSpace, WorkSpace executionWorkspace) {
        Map<String, String> variablesClone = new LinkedHashMap<>(variables);
        Log.log("Executing script "+script+(forItem == null ? "" : " for "+forItem)+" in "+executionWorkspace.getWorkdir());
        List<String> scriptLines = GitBeaver.fileResolver().resolve(
                scriptWorkSpace.getWorkdir(),
                scriptWorkSpace.getAbsolutePath(script+GitBeaver.BEAVER_EXTENSION),
                variablesClone,
                false);
        Expect.notNull(scriptLines).elseFail("Script file does not exist: "+script);
        scriptLines.forEach(line -> executeLine(line, variablesClone, executionWorkspace));
        String result = variablesClone.get(ScriptExecutor.RETURN_VALUE_VARIABLE);
        if (result != null) {
            Log.log("Return value executing {}: {}", script, result);
        }
        return result;
    }

    private void executeLine(String line, Map<String, String> variables, WorkSpace workSpace) {
        Map<String, String> arguments = new LinkedHashMap<>();
        previousLine = currentLine;
        currentLine = line;
        String substituted = VariableSubstitution.substituteVariables(line, variables);
        previousSubstitutedLine = currentSubstitutedLine;
        currentSubstitutedLine = substituted;
        Command command = commandParser.parseCommand(substituted, arguments);
        if (command != null) {
            command.execute(variables, workSpace, arguments);
        }
    }

    public String getPreviousLine() {
        return previousLine;
    }

    public String getPreviousSubstitutedLine() {
        return previousSubstitutedLine;
    }
}
