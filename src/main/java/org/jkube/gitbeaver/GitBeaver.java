package org.jkube.gitbeaver;

import org.jkube.gitbeaver.external.GitCloner;
import org.jkube.gitbeaver.interfaces.ApplicationLogger;
import org.jkube.gitbeaver.interfaces.ApplicationLoggerFactory;
import org.jkube.gitbeaver.interfaces.FileResolver;
import org.jkube.gitbeaver.plugin.PluginManager;
import org.jkube.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GitBeaver {
    private static final String MAIN_VARIABLE = "main";
    public static final String BEAVER_EXTENSION = ".bvr";
    private static final String MAIN_DEFAULT = "main" + BEAVER_EXTENSION;
    private static final String WORKDIR_VARIABLE = "workdir";
    private static final String WORKDIR_DEFAULT = "workdir";

    private static final GitBeaver SINGLETON = new GitBeaver();
    private static final String MAIN_RUN = "main";

    public static GitCloner gitCloner() {
        return SINGLETON.gitCloner;
    }

    public static CommandParser commandParser() {
        return SINGLETON.commandParser;
    }

    public static PluginManager pluginManager() {
        return SINGLETON.pluginManager;
    }

    public static List<ApplicationLoggerFactory> applicationLoggerFactories() {
        return SINGLETON.loggerFactories;
    }

    public static List<ApplicationLogger> activeApplicationLoggers() {
        return SINGLETON.activeLoggers;
    }


    public static FileResolver fileResolver() {
        return SINGLETON.fileResolver;
    }

    public static ScriptExecutor scriptExecutor() {
        return SINGLETON.scriptExecutor;
    }

    public static void setFileResolver(FileResolver fileResolver) {
        SINGLETON.fileResolver = fileResolver;
    }

    public static void run(Map<String, String> args) { SINGLETON.runMain(args); }

    private final GitCloner gitCloner = new GitCloner();
    private final CommandParser commandParser = new CommandParser();

    private final ScriptExecutor scriptExecutor = new ScriptExecutor(commandParser);

    private final PluginManager pluginManager = new PluginManager();

    private final List<ApplicationLoggerFactory> loggerFactories = new ArrayList<>();
    private final List<ApplicationLogger> activeLoggers = new ArrayList<>();

    private FileResolver fileResolver = new DefaultFileResolver();

    private void runMain(Map<String, String> variables) {
        Log.log("Initial variables: "+variables);
        String script = variables.getOrDefault(MAIN_VARIABLE, MAIN_DEFAULT);
        String workspace = variables.getOrDefault(WORKDIR_VARIABLE, WORKDIR_DEFAULT);
        if (loggerFactories.isEmpty()) {
            Log.log("No logger factories available, falling back to DefaultLogger");
            loggerFactories.add(DefaultLogger::new);
        }
        loggerFactories.forEach(lf -> activeLoggers.add(lf.createLogger(MAIN_RUN)));
        scriptExecutor.execute(script, null, variables, new WorkSpace(workspace));
    }

}
