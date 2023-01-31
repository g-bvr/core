package org.jkube.gitbeaver;

import java.util.Map;

/**
 * A simplified base class for commands with the following constraints:
 * - do not use/modify variabbles
 * - no optional argumengs
 * - only one command line pattern variant which is command key words followed by arguments (in sequence of definition)
 */
public abstract class SimpleCommand extends AbstractCommand {

    private final String commandPrefix;

    protected SimpleCommand(String commandPrefix, String description) {
        super(description);
        this.commandPrefix = commandPrefix;
    }

    @Override
    public void postConstruct() {
        StringBuilder commandLine = new StringBuilder(commandPrefix);
        getArguments().keySet().forEach(arg -> commandLine.append(" ").append(arg));
        commandline(commandLine.toString());
    }

    protected abstract void execute(WorkSpace workSpace, Map<String, String> arguments);

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        execute(workSpace, arguments);
    }


}
