package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricOT extends EndPointOT {
    private String name;
    private String description;
    private String baseUnit;
    private MeasurementOT[] measurements;
    private AvailableTagOT[] availableTags;

    public MetricOT() {
        super("metric");
    }

    @Getter
    @Setter
    private static class MeasurementOT {
        private String statistic;
        private long value;
    }

    @Getter
    @Setter
    private static class AvailableTagOT {
        private String tag;
        private String[] values;
    }
}
