package org.jkube.gitbeaver;

import org.jkube.gitbeaver.logging.Log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.jkube.gitbeaver.logging.Log.log;
import static org.jkube.gitbeaver.logging.Log.onException;

public class ExecutionQueue {
    
    private String currentlyExecuted;
    private final Map<String, Runnable> queued = new LinkedHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public synchronized String enqueue(String execution, Runnable trigger) {
        if (currentlyExecuted == null) {
            currentlyExecuted = execution;
            execute(trigger);
            return "Triggered execution of script "+execution;
        }
        if (queued.containsKey(execution)) {
            return "Script for execution "+execution+" is already enqueued, skipping";
        } else {
            queued.put(execution, trigger);
            return "Currently running: "+currentlyExecuted+", script for execution "+execution+" was enqueued";
        }
    }
    private void execute(Runnable trigger) {
        executor.execute(() -> runAndTriggerNext(trigger));
    }

    private void runAndTriggerNext(Runnable trigger) {
        onException(trigger::run).warn("Exception executing triggered script "+currentlyExecuted);
        triggerNext();
    }

    private synchronized void triggerNext() {
        Optional<Map.Entry<String, Runnable>> next = queued.entrySet().stream().findFirst();
        if (next.isPresent()) {
            currentlyExecuted = next.get().getKey();
            queued.remove(currentlyExecuted);
            log("{} in queue, triggering next: {}", queued.size(), currentlyExecuted);
            executor.execute(() -> runAndTriggerNext(next.get().getValue()));
        } else {
            currentlyExecuted = null;
            log("No more queued scripts");
        }
    }

    public void drain() {
        Log.log("Shutting down executor");
        executor.shutdown();
    }
}
