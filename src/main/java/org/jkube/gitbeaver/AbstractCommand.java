package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.logging.Log;

import java.util.*;

public abstract class AbstractCommand implements Command {

    private final List<CommandPattern> commandPatterns = new ArrayList<>();
    private final Map<String, String> argumentDescriptions = new LinkedHashMap<>();

    private final String description;
    private boolean onlyOneVariant = false;

    protected AbstractCommand(String description) {
        this.description = description;
    }

    protected void commandline(String commandPattern) {
        this.commandPatterns.add(CommandParser.createPattern(commandPattern, description));
        onlyOneVariant = true;
    }

    protected void commandlineVariant(String commandPattern, String description) {
        this.commandPatterns.add(CommandParser.createPattern(commandPattern, description));
    }

    protected void argument(String variable, String description) {
        argumentDescriptions.put(variable, description);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void postConstruct() {
        if (getCommandPatterns().isEmpty()) {
            Log.error("No commandline specified, use commandlineVariant() or commandline()");
        }
        if (onlyOneVariant && (getCommandPatterns().size() != 1)) {
            Log.error("Multiple commandline variants specified, use commandlineVariant() instead of commandline()");
        }
        Set<String> argumentsFoundInCommands = new HashSet<>();
        getCommandPatterns().forEach(cp -> argumentsFoundInCommands.addAll(cp.getArguments()));
        if (!argumentsFoundInCommands.equals(argumentDescriptions.keySet())) {
            Log.error("Found argumens {} do not match described arguments {} in step {}", argumentsFoundInCommands, argumentDescriptions.keySet(), getClass().getName());
        }
    }

    @Override
    public Map<String,String> getArguments() {
        return argumentDescriptions;
    }

    @Override
    public List<CommandPattern> getCommandPatterns() {
        return commandPatterns;
    }

}
