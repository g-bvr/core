package org.jkube.gitbeaver;

import org.jkube.gitbeaver.command.GitCloneCommand;
import org.jkube.gitbeaver.command.logging.ErrorCommand;
import org.jkube.gitbeaver.command.logging.LogCommand;
import org.jkube.gitbeaver.command.logging.WarnCommand;
import org.jkube.gitbeaver.command.plugin.PluginCompileCommand;
import org.jkube.gitbeaver.command.plugin.PluginEnableCommand;
import org.jkube.gitbeaver.command.plugin.PluginInstallCommand;
import org.jkube.gitbeaver.command.plugin.PluginsFreezeCommand;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.plugin.SimplePlugin;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
    private static final String COMMENT_PREFIX = "#";

    private static final Pattern KEYWORD_MATCHER = Pattern.compile("[A-Z=>]+");

    private static final Pattern VARIABLE_MATCHER = Pattern.compile("[a-zA-Z0-9_-]+");

    public static final String VARIABLE_REGEX = "([^ ]+)";

    public static final String REST = "*";

    public static final String REST_REGEX = "(.*)";

    private final List<Command> commands = new ArrayList<>();

    public CommandParser() {
        addCommands(new SimplePlugin(
                LogCommand.class,
                WarnCommand.class,
                ErrorCommand.class,
                GitCloneCommand.class,
                PluginCompileCommand.class,
                PluginEnableCommand.class,
                PluginInstallCommand.class,
                PluginsFreezeCommand.class
        ).getCommands());
    }

    public static CommandPattern createPattern(String patternString, String description) {
        String[] split = patternString.split(" +");
        List<String> elements = new ArrayList<>();
        List<String> arguments = new ArrayList<>();
        for (String s : split) {
            if (KEYWORD_MATCHER.matcher(s).matches()) {
                elements.add(s);
            } else if (VARIABLE_MATCHER.matcher(s).matches()) {
                elements.add(VARIABLE_REGEX);
                arguments.add(s);
            } else if (REST.equals(s)) {
                elements.add(REST_REGEX);
                arguments.add(REST);
            } else Log.error("Illegal command element: {}", s);
        }
        return new CommandPattern(String.join("\\s+", elements), description, arguments);
    }

    public void addCommands(Collection<Command> commands) {
        commands.forEach(Command::postConstruct);
        this.commands.addAll(commands);
    }

    public Command parseCommand(String line, Map<String, String> arguments) {
        Expect.size(arguments.entrySet(), 0).elseFail("Non empty arguments list was passed to parseCommand()");
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith(COMMENT_PREFIX)) {
            return null;
        }
        return findCommand(trimmed, arguments);
    }

    public Command findCommand(String line, Map<String, String> arguments) {
        Optional<Command> res = commands.stream()
                .filter(command -> matches(command, line, arguments))
                .findFirst();
        return Expect.present(res).elseFail("No matching command: "+line);
    }

    private boolean matches(Command command, String line, Map<String, String> arguments) {
        return command.getCommandPatterns().stream()
                .anyMatch(cp -> matches(cp, line, arguments));
    }

    private boolean matches(CommandPattern cp, String line, Map<String, String> arguments) {
        Matcher matcher = cp.getPattern().matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        //System.out.println("Matcher "+cp.getPattern()+" matches line "+line);
        if (matcher.groupCount() != cp.getArguments().size()) {
            Log.error("Parsing problem: got {} groups, expected {} arguments", matcher.groupCount(), cp.getArguments().size());
        }
        int i = 0;
        for (String arg : cp.getArguments()) {
            i++;
            //System.out.println("Group "+i+": "+matcher.group(i));
            arguments.put(arg, matcher.group(i));
        }
        return true;
    }

}
