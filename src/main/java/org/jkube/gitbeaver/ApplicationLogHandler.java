package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.ApplicationLogger;
import org.jkube.gitbeaver.interfaces.ApplicationLoggerFactory;
import org.jkube.gitbeaver.interfaces.CombinedApplicationLoggers;
import org.jkube.gitbeaver.interfaces.Command;
import org.jkube.util.Expect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationLogHandler {

    private final List<ApplicationLoggerFactory> loggerFactories = new ArrayList<>();
    private final Map<String, CombinedApplicationLoggers> logger4Run = new LinkedHashMap<>();
    public ApplicationLogHandler() {
        addLogFactory(DefaultLogger::new);
    }

    public void addLogFactory(ApplicationLoggerFactory factory) {
        loggerFactories.add(factory);
    }

    public void createRun(String runId) {
        logger4Run.put(runId, new CombinedApplicationLoggers(createLoggers(runId)));
    }

    private List<ApplicationLogger> createLoggers(String runId) {
        return loggerFactories.stream().map(alf -> alf.createLogger(runId)).collect(Collectors.toList());
    }

    public CombinedApplicationLoggers getLoggerForRun(String runId) {
        CombinedApplicationLoggers res = logger4Run.get(runId);
        Expect.notNull(res).elseFail("Did not find logger for run: "+runId);
        return res;
    }
}
