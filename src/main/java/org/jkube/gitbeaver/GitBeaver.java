package org.jkube.gitbeaver;

import org.jkube.gitbeaver.applicationlog.ApplicationLogHandler;
import org.jkube.gitbeaver.applicationlog.CombinedApplicationLoggers;
import org.jkube.gitbeaver.external.GitCloner;
import org.jkube.gitbeaver.interfaces.FileResolver;
import org.jkube.gitbeaver.plugin.CorePlugin;
import org.jkube.gitbeaver.plugin.PluginManager;
import org.jkube.logging.Log;

import java.nio.file.Path;
import java.util.Map;

public class GitBeaver {
    public static final String BEAVER_EXTENSION = ".bvr";

    public static final Path SECRETS_DIRECTORY = Path.of("/secret/");

    private static final String MAIN_VARIABLE = "main";
    private static final String MAIN_DEFAULT = "main";
    public static final String RUN_ID_VARIABLE = "run";

    private static final String RUN_ID_DEFAULT = "init";

    private static final String WORKDIR_VARIABLE = "workdir";
    private static final String WORKDIR_DEFAULT = "workdir";

    private static final GitBeaver SINGLETON = new GitBeaver();

    public static GitCloner gitCloner() {
        return SINGLETON.gitCloner;
    }

    public static CommandParser commandParser() {
        return SINGLETON.commandParser;
    }

    public static PluginManager pluginManager() {
        return SINGLETON.pluginManager;
    }

    public static FileResolver fileResolver() {
        return SINGLETON.fileResolver;
    }

    public static ScriptExecutor scriptExecutor() {
        return SINGLETON.scriptExecutor;
    }

    public static ApplicationLogHandler applicationLogHandler() {
        return SINGLETON.applicationLogHandler;
    }

    public static CombinedApplicationLoggers getApplicationLogger(Map<String, String> variables) {
        return SINGLETON.applicationLogHandler.getLoggerForRun(variables.get(GitBeaver.RUN_ID_VARIABLE));
    }
    public static void setFileResolver(FileResolver fileResolver) {
        SINGLETON.fileResolver = fileResolver;
    }

    public static void run(Map<String, String> args) { SINGLETON.runMain(args); }

    private final GitCloner gitCloner = new GitCloner();
    private final CorePlugin corePlugin = new CorePlugin();
    private final CommandParser commandParser = new CommandParser(corePlugin);

    private final ScriptExecutor scriptExecutor = new ScriptExecutor(commandParser);

    private final PluginManager pluginManager = new PluginManager(corePlugin);

    private FileResolver fileResolver = new DefaultFileResolver();

    private final ApplicationLogHandler applicationLogHandler = new ApplicationLogHandler();

    private void runMain(Map<String, String> variables) {
        String script = withDefault(variables, MAIN_VARIABLE, MAIN_DEFAULT);
        String workspace = withDefault(variables, WORKDIR_VARIABLE, WORKDIR_DEFAULT);
        String runId = withDefault(variables, RUN_ID_VARIABLE, RUN_ID_DEFAULT);
        Log.log("Initial variables: "+variables);
        applicationLogHandler.createRun(runId);
        scriptExecutor.execute(script, null, variables, new WorkSpace(workspace));
    }

    private String withDefault(Map<String, String> variables, String key, String defaultValue) {
        variables.putIfAbsent(key, defaultValue);
        return variables.get(key);
    }

}
