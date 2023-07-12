package org.jkube.gitbeaver;

import org.jkube.logging.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class FailureHandler implements org.jkube.application.FailureHandler  {
    private static final Thread MAIN_THREAD = Thread.currentThread();
    public static final AtomicInteger NUM_CATCHING_BLOCKS = new AtomicInteger(0);

    @Override
    public void fail(String message, int code) {
        Log.exception(new RuntimeException("Failure captured: "+message));
        if (Thread.currentThread().equals(MAIN_THREAD)) {
            Log.error("Failure in main thread: {}", message);
            if (NUM_CATCHING_BLOCKS.get() == 0) {
                Log.error("Not inside catching block: terminating VM with error code: {}", code);
                System.exit(code);
            } else {
                Log.error("Inside catching block: throwing exception");
                throw new RuntimeException(message);
            }
        } else {
            Log.error("Failure in execution thread: {}", message);
        }
    }
}
