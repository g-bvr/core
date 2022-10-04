package org.jkube.gitbeaver.util;

import org.jkube.util.Expect;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Environment {

    public static final String GITBEAVER_CLASSPATH = "GITBEAVER_CLASSPATH";

    public static final String GITBEAVER_DEBUG = "GITBEAVER_DEBUG";
    private static final Map<String, String> DEFAULTS = new LinkedHashMap<>();

    public static Path classPath() {
      return Path.of(getEnv(GITBEAVER_CLASSPATH));
    }

    public static boolean debug() {
        return getEnv(GITBEAVER_DEBUG).equalsIgnoreCase("true)");
    }

    public static void setDefault(String key, String value) {
        DEFAULTS.put(key, value);
    }

    private static String getEnv(String key) {
        String res = System.getenv().getOrDefault(key, DEFAULTS.get(key));
        Expect.notNull(res).elseFail("Environment variable not set: "+key);
        return res;
    }

}
