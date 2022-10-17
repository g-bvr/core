package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.interfaces.Plugin;
import org.jkube.gitbeaver.util.FileUtil;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.jkube.logging.Log.onException;

public class PluginManager {

    private static final Path PLUGIN_LIST = Path.of("/plugins.txt");
    private static final String FREEZE_MARKER = "[FREEZE]";
    static boolean pluginsFrozen = false;

    private final Map<String, Plugin> plugins = new LinkedHashMap<>();

    public Plugin getPlugin(String className) {
        return plugins.get(className);
    }

    public void shutdown() {
        plugins.values().forEach(Plugin::shutdown);
    }

    public static boolean checkPluginsNotFrozen() {
        Expect.isFalse(pluginsFrozen).elseFail("Plugins are frozen");
        return !pluginsFrozen;
    }

    public static void setPluginsFrozen(boolean writeFreezeMarker) {
        if (pluginsFrozen) {
            return;
        }
        pluginsFrozen = true;
        Log.log("Plugins are now frozen");
        if (writeFreezeMarker) {
            FileUtil.append(FREEZE_MARKER, PLUGIN_LIST.toFile());
        }
    }

    public boolean enable(String pluginClass) {
        if (checkPluginsNotFrozen()) {
            Class<?> cls = onException(() -> Class.forName(pluginClass, true, getClass().getClassLoader()))
                    .fail("Could not load class: " + pluginClass);
            Plugin plugin = onException(() -> (Plugin) cls.getConstructor().newInstance())
                    .fail("Could not instantiatxe class: " + pluginClass);
            if (plugins.containsKey(pluginClass)) {
                return false;
            }
            plugins.put(pluginClass, plugin);
            plugin.init();
            GitBeaver.commandParser().addCommands(plugin.getCommands());
            return true;
        }
        return false;
    }

    public void enableAllAvailablePlugins() {
        StringBuilder sb = new StringBuilder();
        if (PLUGIN_LIST.toFile().exists()) {
            sb.append("Available plugins:");
            FileUtil.readLines(PLUGIN_LIST).forEach(line -> sb.append("\n").append(activatePluginMessage(line)));
            Log.log(sb.toString());
        } else {
            Log.log("No plugins list found at: "+PLUGIN_LIST);
        }
    }

    private String activatePluginMessage(String line) {
        if (line.equals(FREEZE_MARKER)) {
            PluginManager.setPluginsFrozen(false);
        }
        String[] split = line.split(" ");
        Expect.equal(2, split.length).elseFail("Illegal plugin list format: "+line);
        boolean success = GitBeaver.pluginManager().enable(split[1]);
        return split[0] + " --> "+(success ? "OK" : "FAILED");
    }

}
