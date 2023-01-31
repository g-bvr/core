package org.jkube.gitbeaver;

import org.jkube.gitbeaver.command.GitCloneCommand;
import org.jkube.gitbeaver.command.logging.LogCommand;
import org.jkube.gitbeaver.command.plugin.PluginCompileCommand;
import org.jkube.gitbeaver.command.plugin.PluginsFreezeCommand;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.gitbeaver.util.test.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;

public class CommandParserTest {

    public static final String SRC_TEST_RESOURCES = "src/test/resources";
    public static final String WORKDIR = "workdir";

    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    void parseLog() {
        testParse("LOG", LogCommand.class, Map.of());
        testParse("LOG abc", LogCommand.class, Map.of(REST, "abc"));
        testParse("LOG abc def", LogCommand.class, Map.of(REST, "abc def"));
        testParse("LOG :-@", LogCommand.class, Map.of(REST, ":-@"));
        testParse("LOG  a  b", LogCommand.class, Map.of(REST, " a  b"));
        testParse("LOG   ", LogCommand.class, Map.of(REST, "  "));
    }

    @Test
    void parseGitClone() {
        testParse("GIT CLONE", null, null);
        testParse("GIT CLONE url repo", GitCloneCommand.class, Map.of("baseurl", "url", "repository", "repo"));
        testParse("GIT CLONE url repo 1.0", GitCloneCommand.class, Map.of("baseurl", "url", "repository", "repo", "tag", "1.0"));
        testParse("GIT CLONE url repo 1.0 ?", null, null);
    }

    @Test
    void parsePluginCompile() {
        testParse("PLUGIN COMPILE", null, null);
        testParse("PLUGIN COMPILE dir", PluginCompileCommand.class, Map.of("directory", "dir"));
    }

    @Test
    void parsePluginEnable() {
        testParse("PLUGIN ENABLE", null, null);
        testParse("PLUGIN ENABLE name", PluginCompileCommand.class, Map.of("plugin", "name"));
    }

    @Test
    void parsePluginsFreeze() {
        testParse("PLUGINS FREEZE", PluginsFreezeCommand.class, Map.of());
        testParse("PLUGINS FREEZE ?", null, null);
    }

    @Test
    void parseUnknownCommand() {
        testParse("UNKNOWN", null, null);
        testParse("UNKNOWN abc", null, null);
        testParse("UNKNOWN LOG", null, null);
    }

    @Test
    void parseEmptyLine() {
        CommandParser parser = new CommandParser();
        Map<String, String> parsedargs = new HashMap<>();
        Command pc = parser.parseCommand("", parsedargs);
        Assertions.assertNull(pc);
        TestUtil.assertNoFailures();
    }

    void testParse(String commandLine, Class<? extends Command> expectedCommand, Map<String,String> expectedArgs) {
        CommandParser parser = new CommandParser();
        Map<String, String> parsedargs = new LinkedHashMap<>();
        Command pc = parser.parseCommand(commandLine, parsedargs);
        if (expectedCommand != null) {
            Assertions.assertNotNull(pc);
            TestUtil.assertNoFailures();
            Assertions.assertNotNull(pc);
            Assertions.assertEquals(parsedargs, expectedArgs);
        } else {
            Assertions.assertNull(pc);
            TestUtil.assertFailure();
        }
    }

}
