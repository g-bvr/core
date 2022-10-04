package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.jkube.logging.Log.onException;

/**
 * Usage: git clone providerUrl repositoryName [tag]
 */
public class GitCloneCommand implements Command {

    @Override
    public int minNumArgs() {
        return 2;
    }

    @Override
    public Integer maxNumArgs() {
        return 3;
    }

    @Override
    public List<String> keywords() {
        return List.of("git", "clone");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        URL url = onException(() -> new URL(arguments.get(0))).fail("Could not parse url "+arguments.get(0));
        String repository = arguments.get(1);
        String tag = arguments.size() == 3 ? arguments.get(2) : null;
        GitBeaver.gitCloner().clone(workSpace.getWorkdir(), url, repository, tag);
    }

}
