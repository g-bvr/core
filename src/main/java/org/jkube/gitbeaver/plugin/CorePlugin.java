package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.command.GitCloneCommand;
import org.jkube.gitbeaver.command.logging.ErrorCommand;
import org.jkube.gitbeaver.command.logging.LogCommand;
import org.jkube.gitbeaver.command.logging.WarnCommand;
import org.jkube.gitbeaver.command.plugin.PluginCompileCommand;
import org.jkube.gitbeaver.command.plugin.PluginEnableCommand;
import org.jkube.gitbeaver.command.plugin.PluginInstallCommand;
import org.jkube.gitbeaver.command.plugin.PluginsFreezeCommand;

public class CorePlugin extends SimplePlugin {
    public CorePlugin() {
        super("""
                            Collects the most elementary commands (logging, simple git cloning and plugin management).                 
                        """,
                LogCommand.class,
                WarnCommand.class,
                ErrorCommand.class,
                GitCloneCommand.class,
                PluginCompileCommand.class,
                PluginEnableCommand.class,
                PluginInstallCommand.class,
                PluginsFreezeCommand.class
        );
    }

}
