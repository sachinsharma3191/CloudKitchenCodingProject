package com.cloud.kitchen.util;

import java.util.concurrent.*;
public class ExecutorServiceUtility {

    public ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }
}
