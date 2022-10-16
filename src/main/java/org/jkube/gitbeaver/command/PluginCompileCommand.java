package org.jkube.gitbeaver.command;

import org.jkube.gitbeaver.util.Environment;
import org.jkube.gitbeaver.SimpleCommand;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.plugin.PluginCompiler;

import java.nio.file.Path;
import java.util.List;

import static org.jkube.logging.Log.onException;

/**
 * Usage: plugin compile soureDir
 */
public class PluginCompileCommand extends SimpleCommand {

    public PluginCompileCommand() {
        super(1, "plugin", "compile");
    }

    @Override
    protected void execute(WorkSpace workSpace, List<String> arguments) {
        Path sourcePath = workSpace.getAbsolutePath(arguments.get(0));
        new PluginCompiler(Environment.classPath()).compile(sourcePath);
    }

}
