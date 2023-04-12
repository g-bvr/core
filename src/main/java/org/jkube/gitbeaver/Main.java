package org.jkube.gitbeaver;

import org.jkube.application.Application;
import org.jkube.logging.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final Thread MAIN_THREAD = Thread.currentThread();

    public static void main(String[] args) {
        Application.setFailureHandler((message, code) -> {
            Log.exception(new RuntimeException("Failure captured: "+message));
            if (Thread.currentThread().equals(MAIN_THREAD)) {
                Log.error("Failure in main: {}", message);
                Log.error("Terminating VM with error code: {}", code);
                System.exit(code);
            } else {
                Log.error("Failure in execution thread: {}", message);
            }
        });
        Log.log("Running git-beaver {}", Main.class.getPackage().getImplementationVersion());
        GitBeaver.pluginManager().enableAllAvailablePlugins();
        GitBeaver.run(parseArgs(args));
        Log.log("Done.");
    }


    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> res = new LinkedHashMap<>();
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length != 2) {
                Application.fail("argument is not of form key=value: "+arg);
            }
            res.put(split[0].trim(), split[1].trim());
        }
        return res;
    }

}