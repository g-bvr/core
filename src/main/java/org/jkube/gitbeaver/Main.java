package org.jkube.gitbeaver;

import org.jkube.gitbeaver.application.Application;
import org.jkube.gitbeaver.logging.Log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        Application.setFailureHandler(new FailureHandler());
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