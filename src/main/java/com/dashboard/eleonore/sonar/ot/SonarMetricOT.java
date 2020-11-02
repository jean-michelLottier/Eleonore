package com.dashboard.eleonore.sonar.ot;

import java.util.List;

public class SonarMetricOT {
    private String metric;
    private String value;
    private boolean bestValue;
    private List<SonarPeriodOT> periods;
    private SonarPeriodOT period;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isBestValue() {
        return bestValue;
    }

    public void setBestValue(boolean bestValue) {
        this.bestValue = bestValue;
    }

    public List<SonarPeriodOT> getPeriods() {
        return periods;
    }

    public void setPeriods(List<SonarPeriodOT> periods) {
        this.periods = periods;
    }

    public SonarPeriodOT getPeriod() {
        return period;
    }

    public void setPeriod(SonarPeriodOT period) {
        this.period = period;
    }
}
