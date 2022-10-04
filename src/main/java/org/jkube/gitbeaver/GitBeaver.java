package org.jkube.gitbeaver;

import org.jkube.gitbeaver.githandler.GitCloner;
import org.jkube.gitbeaver.interfaces.FileResolver;
import org.jkube.gitbeaver.interfaces.LogListener;
import org.jkube.gitbeaver.plugin.PluginManager;
import org.jkube.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GitBeaver {
    private static final String MAIN_VARIABLE = "main";
    private static final String MAIN_DEFAULT = "main.bvr";
    private static final String WORKDIR_VARIABLE = "workdir";
    private static final String WORKDIR_DEFAULT = ".";

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

    public static List<LogListener> logListeners() {
        return SINGLETON.logListeners;
    }

    public static FileResolver fileResolver() {
        return SINGLETON.fileResolver;
    }

    public static void setFileResolver(FileResolver fileResolver) {
        SINGLETON.fileResolver = fileResolver;
    }

    public static void run(Map<String, String> args) { SINGLETON.runMain(args); }

    private final GitCloner gitCloner = new GitCloner();
    private final CommandParser commandParser = new CommandParser();

    private final ScriptExecutor scriptExecutor = new ScriptExecutor(commandParser);

    private final PluginManager pluginManager = new PluginManager();

    private final List<LogListener> logListeners = new ArrayList<>();

    private FileResolver fileResolver = new DefaultFileResolver();

    private void runMain(Map<String, String> variables) {
        Log.log("Initial variables: "+variables);
        String script = variables.getOrDefault(MAIN_VARIABLE, MAIN_DEFAULT);
        String workspace = variables.getOrDefault(WORKDIR_VARIABLE, WORKDIR_DEFAULT);
        scriptExecutor.execute(script, null, variables, new WorkSpace(workspace));
    }

}
