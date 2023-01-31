package org.jkube.gitbeaver.plugin;

import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.WorkSpace;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.gitbeaver.util.test.TestUtil;
import org.jkube.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PluginTest {

    public static final String CLASS_PATH = "target/classes";
    public static final String PLUGIN_SOURCE_PATH = "src/test/resources/plugin";
    public static final String PLUGIN_CLASS = "org.jkube.gitbeaver.TestPlugin";
    public static final String PLUGIN_CLASS_FILE = "/org/jkube/gitbeaver/TestPlugin.class";


    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    public void compilerTest() {
        PluginCompiler compiler = new PluginCompiler(Path.of(CLASS_PATH));
        compiler.compile(Path.of(PLUGIN_SOURCE_PATH));
        Assertions.assertTrue(new File(CLASS_PATH+PLUGIN_CLASS_FILE).exists());
        TestUtil.assertNoFailures();
    }

    @Test
    public void classLoaderTest() {
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, CLASS_PATH);
        PluginCompiler compiler = new PluginCompiler(Path.of(CLASS_PATH));
        compiler.compile(Path.of(PLUGIN_SOURCE_PATH));
        Log.log("Done compiling");
        GitBeaver.pluginManager().enable(PLUGIN_CLASS);
        Command command = GitBeaver.commandParser().parseCommand("test", new HashMap<>());
        Assertions.assertNotNull(command);
        command.execute(null, null, null);
        TestUtil.assertNoFailures();
    }

    @Test
    public void noCompileIfFrozen() {
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, CLASS_PATH);
        PluginCompiler compiler = new PluginCompiler(Path.of(CLASS_PATH));
        Map<String,String> arguments = new HashMap<>();
        Command freezecommand = GitBeaver.commandParser().parseCommand("PLUGINS FREEZE", arguments);
        Assertions.assertNotNull(freezecommand);
        Assertions.assertTrue(arguments.isEmpty());
        freezecommand.execute(null, null, arguments);
        Command compilecommand = GitBeaver.commandParser().parseCommand("PLUGIN COMPILE "+PLUGIN_SOURCE_PATH, arguments);
        Assertions.assertNotNull(freezecommand);
        WorkSpace workSpace = new WorkSpace(".");
        Assertions.assertEquals(1, arguments.size());
        TestUtil.assertNoFailures();
        compilecommand.execute(null, workSpace, arguments);
        TestUtil.assertFailure();
        PluginManager.pluginsFrozen = false;
    }

}
