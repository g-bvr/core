package org.jkube.gitbeaver;

import java.util.List;
import java.util.regex.Pattern;

public class CommandPattern {

    private final String description;
    private final Pattern pattern;
    private final List<String> arguments;

    public CommandPattern(String patternString, String description, List<String> arguments) {
        this.pattern = Pattern.compile(patternString);
        this.description = description;
        this.arguments = arguments;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
