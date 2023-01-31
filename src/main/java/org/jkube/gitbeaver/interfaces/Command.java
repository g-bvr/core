package org.jkube.gitbeaver.interfaces;

import org.jkube.gitbeaver.CommandPattern;
import org.jkube.gitbeaver.WorkSpace;

import java.util.List;
import java.util.Map;

public interface Command {

    void postConstruct();

    String getDescription();

    Map<String,String> getArguments();

    List<CommandPattern> getCommandPatterns();

    void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments);
}
