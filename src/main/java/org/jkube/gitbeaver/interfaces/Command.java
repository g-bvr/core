package org.jkube.gitbeaver.interfaces;

import org.jkube.gitbeaver.WorkSpace;

import java.util.List;
import java.util.Map;

public interface Command {

    int minNumArgs();

    Integer maxNumArgs();

    List<String> keywords();

    void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments);
}
