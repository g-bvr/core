package org.jkube.gitbeaver;

import org.jkube.application.Application;
import org.jkube.gitbeaver.command.*;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.util.Expect;

import java.util.*;

public class CommandParser {
    private static final String COMMENT_PREFIX = "#";

    private final List<Command> commands = new ArrayList<>();

    public CommandParser() {
        addCommands(List.of(
                new LogCommand(),
                new GitCloneCommand(),
                new PluginCompileCommand(),
                new PluginEnableCommand(),
                new PluginsFreezeCommand()
        ));
    }

    public void addCommands(Collection<Command> commands) {
        this.commands.addAll(commands);
    }

    public Command parseCommand(String line, List<String> arguments) {
        Expect.size(arguments,0).elseFail("Non empty arguments list was passed to parseCommand()");
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith(COMMENT_PREFIX)) {
            return null;
        }
        String[] split = trimmed.split(" ");
        Command res = findCommand(split);
        if (res == null) {
            return null;
        }
        copyArguments(split, res.keywords().size(), arguments);
        return checkNumArgs(res, arguments.size()) ? res : null;
    }

    private boolean checkNumArgs(Command command, int size) {
        int min = command.minNumArgs();
        if (size < min) {
            Application.fail("The minimum number of arguments for "
                    + String.join(" ", command.keywords())
                    + " is " + min
                    + ", received: " + size);
            return false;
        }
        Integer max = command.maxNumArgs();
        if ((max != null) && (size > max)) {
            Application.fail("The maximum number of arguments for "
                    + String.join(" ", command.keywords())
                    + " is " + max
                    + ", received: " + size);
            return false;
        }
        return true;
    }

    private Command findCommand(String[] split) {
        Optional<Command> res = commands.stream()
                .filter(command -> hasKeywords(command.keywords(), split))
                .findFirst();
        return Expect.present(res).elseFail("No such command: "+ Arrays.toString(split));
    }

    private boolean hasKeywords(List<String> keywords, String[] command) {
        if (command.length < keywords.size()) {
            return false;
        }
        for (int i = 0; i < keywords.size(); i++) {
            if (!keywords.get(i).equalsIgnoreCase(command[i])) {
                return false;
            }
        }
        return true;
    }

    private void copyArguments(String[] command, int first, List<String> arguments) {
        arguments.addAll(Arrays.asList(command).subList(first, command.length));
    }


}
