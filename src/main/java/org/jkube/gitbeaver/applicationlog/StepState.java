package org.jkube.gitbeaver.applicationlog;

public enum StepState {
    RUNNING, DIDNOTHING, DIDSOMETHING, SUCCESS, WARNING, ERROR;

    public StepState combine(StepState other) {
        return compareTo(other) > 0 ? this : other;
    }
}
