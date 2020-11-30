package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LoggersOT extends EndPointOT {
    private String[] levels;
    private Map<String, LoggerOT> loggers;
    private Map<String, GroupOT> groups;

    public LoggersOT() {
        super("loggers");
    }

    @Getter
    @Setter
    private static class LoggerOT {
        private String configuredLevel;
        private String effectiveLevel;
    }

    @Getter
    @Setter
    private static class GroupOT {
        private String configuredLevel;
        private String[] members;
    }
}
