package org.jkube.gitbeaver.applicationlog;

import org.jkube.gitbeaver.interfaces.ApplicationLogger;
import org.jkube.gitbeaver.interfaces.LogConsole;
import org.jkube.logging.Log;
import org.jkube.util.Expect;

import java.util.List;

public class CombinedApplicationLoggers implements ApplicationLogger {

    private final List<ApplicationLogger> applicationLoggers;

    public CombinedApplicationLoggers(List<ApplicationLogger> loggers) {
        applicationLoggers = loggers;
    }

    @Override
    public LogConsole createSubConsole() {
        return new CombinedLogConsole(applicationLoggers.stream().map(ApplicationLogger::createSubConsole));
    }

    @Override
    public void createStep(String title) {
        applicationLoggers.forEach(al -> al.createStep(title));
    }

    @Override
    public void setStepState(StepState state) {
        applicationLoggers.forEach(al -> al.setStepState(state));
    }

    @Override
    public void closeStep() {
        applicationLoggers.forEach(ApplicationLogger::closeStep);
    }

    @Override
    public void log(String line) {
        applicationLoggers.forEach(al -> al.log(line));
    }

    @Override
    public void warn(String line) {
        applicationLoggers.forEach(al -> al.warn(line));
    }

    @Override
    public void error(String line) {
        applicationLoggers.forEach(al -> al.warn(line));
    }

    @Override
    public String getCollectedLogs() {
        Expect.size(applicationLoggers, 1).elseFail("Could not get combined logs for multiple loggers");
        return applicationLoggers.get(0).getCollectedLogs();
    }

    public String getCollectedLogs(String applicationLoggerClassName) {
        for (ApplicationLogger al : applicationLoggers) {
            if (al.getClass().getName().equals(applicationLoggerClassName)) {
                return al.getCollectedLogs();
            }
        }
        Log.warn("Could not get logs, logger class not registered: "+applicationLoggerClassName);
        return null;
    }

    public void add(ApplicationLogger logger) {
        applicationLoggers.add(logger);
    }
}
