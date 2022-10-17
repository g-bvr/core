package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.ApplicationLogger;
import org.jkube.gitbeaver.interfaces.LogConsole;
import org.jkube.gitbeaver.interfaces.StepState;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.util.ArrayList;
import java.util.List;

public class DefaultLogger implements ApplicationLogger,LogConsole {

    private static final int INDENT_SPACES = 3;
    int currentLevel = 0;

    List<String> logLines = new ArrayList<>();
    public DefaultLogger(String runIdentifier) {
        Log.log("Creating default logger for tun id: "+runIdentifier);
    }

    @Override
    public LogConsole createSubConsole() {
        return this;
    }

    @Override
    public void createStep(String title) {
        currentLevel++;
        String line = "-".repeat(title.length());
        indentLog("");
        indentLog(line);
        indentLog(title);
        indentLog(line);
        indentLog("");
    }

    @Override
    public void setStepState(StepState state) {
        indentError("Step is marked with state: "+state);
    }

    @Override
    public void closeStep() {
        indentLog("Step was closed");
        currentLevel--;
        Expect.atLeast(currentLevel, 0).elseFail("More steps closed than created");
    }

    @Override
    public List<String> getCollectedLogs() {
        return logLines;
    }

    @Override
    public void command(String message) {
        indentLog(message);
    }

    @Override
    public void ignore(String message) {
        indentLog(message);
    }

    @Override
    public void warn(String message) {
        indentWarn(message);
    }

    @Override
    public void success(String message) {
        indentLog(message);
    }

    @Override
    public void error(String message) {
        indentError(message);
    }

    public void log(String message) {
        indentLog(message);
    }

    private String indent() {
        return" ".repeat(currentLevel*INDENT_SPACES);
    }

    private void indentLog(String message) {
        Log.log(indent()+message);
    }

    private void indentWarn(String message) {
        Log.warn(indent()+message);
    }

    private void indentError(String message) {
        Log.error(indent()+message);
    }

}
