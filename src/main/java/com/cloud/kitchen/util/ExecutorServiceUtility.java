package com.cloud.kitchen.util;

import java.util.concurrent.*;
public class ExecutorServiceUtility {

    public ScheduledExecutorService scheduledExecutorService;

    public ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }

    public void shutdown(){
        try {
            if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduledExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
