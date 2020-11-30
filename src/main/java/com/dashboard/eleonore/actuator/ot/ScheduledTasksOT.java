package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduledTasksOT extends EndPointOT {
    private CronOT[] cron;
    private FixedRateDelayOT[] fixedDelay;
    private FixedRateDelayOT[] fixedRate;
    private CustomOT[] custom;

    public ScheduledTasksOT() {
        super("scheduledtasks");
    }

    @Getter
    @Setter
    private static class CronOT {
        private RunnableOT runnable;
        private String expression;
    }

    @Getter
    @Setter
    private static class FixedRateDelayOT {
        private RunnableOT runnable;
        private long initialDelay;
        private long interval;
    }

    @Getter
    @Setter
    private static class CustomOT {
        private RunnableOT runnable;
        private String trigger;
    }

    @Getter
    @Setter
    private static class RunnableOT {
        private String target;
    }
}
