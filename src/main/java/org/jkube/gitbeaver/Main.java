package org.jkube.gitbeaver;

import org.jkube.application.Application;
import org.jkube.logging.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.jkube.logging.Log.log;

public class Main {
    public static void main(String[] args) {
        Application.setFailureHandler((message, code) -> {
            Log.error("Critical failure: {}, terminating VM with exit code {}", message, code);
            System.exit(code);
        });
        Log.log("Running gitresolver {}", Main.class.getPackage().getImplementationVersion());
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