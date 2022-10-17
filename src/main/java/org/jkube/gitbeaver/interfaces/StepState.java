package org.jkube.gitbeaver.interfaces;

public enum StepState {
    DIDNOTHING, DIDSOMETHING, SUCCESS, WARNING, ERROR;

    public StepState combine(StepState other) {
        return compareTo(other) > 0 ? this : other;
    }
}
