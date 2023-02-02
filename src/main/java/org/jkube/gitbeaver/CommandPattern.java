package org.jkube.gitbeaver;

import java.util.List;
import java.util.regex.Pattern;

public class CommandPattern {

    private final String text;
    private final String description;
    private final Pattern pattern;
    private final List<String> arguments;

    public CommandPattern(String text, String patternString, String description, List<String> arguments) {
        this.text = text;
        this.pattern = Pattern.compile(patternString);
        this.description = description;
        this.arguments = arguments;
    }

    public String getText() {
        return text;
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
