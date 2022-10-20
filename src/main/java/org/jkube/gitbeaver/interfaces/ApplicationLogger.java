package org.jkube.gitbeaver.interfaces;

import java.util.List;

public interface ApplicationLogger extends SimpleLogger {

    LogConsole createSubConsole();

    void createStep(String title);

    void setStepState(StepState state);

    void closeStep();

    List<String> getCollectedLogs();
}
