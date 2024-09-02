package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.AbstractCommand;
import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;

import java.net.URL;
import java.util.Map;

import static org.jkube.gitbeaver.logging.Log.onException;

/**
 * Usage: git clone providerUrl repositoryName [tag]
 */
public class GitCloneCommand extends AbstractCommand {

    private static final String BASE_URL = "baseurl";
    private static final String REPOSITORY = "repository";

    private static final String TAG = "tag";

    public GitCloneCommand() {
        super("""
                Simple git cloning command.
            """
        );
        argument(BASE_URL, "The url prefix of the repository (not including the actual repo name)");
        argument(REPOSITORY, "The name of the repository (which together with the base url constitutes the url of the repository)");
        argument(TAG, "Optional argument to specify either a branch or a tag which shall be checked out (if omitted the default branch will be used)");
        commandlineVariant("GIT CLONE "+BASE_URL+" "+REPOSITORY+" "+TAG, """
                Clones the head of a branch or a tag of a git repository.
            """);
        commandlineVariant("GIT CLONE "+BASE_URL+" "+REPOSITORY, """
                Clones the default branch of a git repository.
            """);
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        String urlString = arguments.get(BASE_URL);
        String repository = arguments.get(REPOSITORY);
        String tag = arguments.get(TAG);
        URL url = onException(() -> new URL(urlString)).fail("Could not parse url "+urlString);
        GitBeaver.gitCloner().clone(workSpace.getWorkdir(), url, repository, tag, GitBeaver.getApplicationLogger(variables));
    }

}
