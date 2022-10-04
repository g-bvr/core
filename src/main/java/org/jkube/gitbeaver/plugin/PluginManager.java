package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.interfaces.Plugin;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.jkube.logging.Log.exception;
import static org.jkube.logging.Log.onException;

public class PluginManager {

    static boolean pluginsFrozen = false;

    private final ClassLoader classLoader = createClassLoader();

    private final List<Plugin> plugins = new ArrayList<>();

    public void shutdown() {
        plugins.forEach(Plugin::shutdown);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public static boolean checkPluginsNotFrozen() {
        Expect.isFalse(pluginsFrozen).elseFail("Plugins are frozen");
        return !pluginsFrozen;
    }

    public static void setPluginsFrozen() {
        pluginsFrozen = true;
    }

    public ClassLoader createClassLoader() {
        URL url = onException(() -> Environment.classPath().toFile().toURI().toURL())
                .fail("Could not convert to URL: "+Environment.classPath());
        Log.log("Using URLClassloader: "+url);
        return URLClassLoader.newInstance(new URL[] { url });
    }

    public void enable(String pluginClass) {
        if (checkPluginsNotFrozen()) {
            Class<?> cls = onException(() -> Class.forName(pluginClass, true, classLoader))
                    .fail("Could not load class: " + pluginClass);
            Plugin plugin = onException(() -> (Plugin) cls.getConstructor().newInstance())
                    .fail("Could not instantiatxe class: " + pluginClass);
            plugins.add(plugin);
            plugin.init();
            GitBeaver.commandParser().addCommands(plugin.getCommands());
        }
    }

}
