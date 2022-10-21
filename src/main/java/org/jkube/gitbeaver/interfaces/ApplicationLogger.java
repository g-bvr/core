package org.jkube.gitbeaver.interfaces;

import org.jkube.gitbeaver.applicationlog.StepState;

public interface ApplicationLogger extends SimpleLogger {

    LogConsole createSubConsole();

    void createStep(String title);

    void setStepState(StepState state);

    void closeStep();

    String getCollectedLogs();
}
