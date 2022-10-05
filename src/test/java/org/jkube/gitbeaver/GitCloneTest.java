package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GitCloneTest {

    public static final String SRC_TEST_RESOURCES = "src/test/resources";
    public static final String WORKDIR = "workdir";

    @BeforeAll
    static void beforeTests() { TestUtil.beforeTests(); }

    @BeforeEach
    void beforeEachTests() { TestUtil.beforeEachTest(); }

    @Test
    void parseLog() {
        testParse("log", "", true);
        testParse("log", "abc", true);
        testParse("log", "abc def ghi jkl mno pqr", true);
    }

    @Test
    void parseGitClone() {
        testParse("git clone", "", false);
        testParse("git clone", "url repo", true);
        testParse("git clone", "url repo tag", true);
        testParse("git clone", "url repo tag ?", false);
    }

    @Test
    void parsePluginCompile() {
        testParse("plugin compile", "", false);
        testParse("plugin compile", "dir", true);
        testParse("plugin compile", "dir ?", false);
    }

    @Test
    void parsePluginEnable() {
        testParse("plugin enable", "", false);
        testParse("plugin enable", "class", true);
        testParse("plugin enable", "class ?", false);
    }

    @Test
    void parsePluginsFreeze() {
        testParse("plugins freeze", "", true);
        testParse("plugins freeze", "?", false);
    }

    @Test
    void parseUnknownCommand() {
        testParse("unknown", "", false);
        testParse("unknown", "abc", false);
    }

    @Test
    void parseEmptyLine() {
        CommandParser parser = new CommandParser();
        List<String> parsedargs = new ArrayList<>();
        Command pc = parser.parseCommand("", parsedargs);
        Assertions.assertNull(pc);
        TestUtil.assertNoFailures();
    }

    void testParse(String command, String args, boolean ok) {
        CommandParser parser = new CommandParser();
        List<String> parsedargs = new ArrayList<>();
        Command pc = parser.parseCommand(command+" "+args, parsedargs);
        if (ok) {
            Assertions.assertNotNull(pc);
            TestUtil.assertNoFailures();
            Assertions.assertEquals(command, String.join(" ", pc.keywords()));
            Assertions.assertEquals(args, String.join(" ", parsedargs));
        } else {
            Assertions.assertNull(pc);
            TestUtil.assertFailure();
        }
    }

}
