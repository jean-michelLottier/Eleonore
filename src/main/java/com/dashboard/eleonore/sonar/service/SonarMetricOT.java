package com.dashboard.eleonore.sonar.service;

public class SonarMetricOT {
    private String metric;
    private double value;
    private boolean bestValue;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isBestValue() {
        return bestValue;
    }

    public void setBestValue(boolean bestValue) {
        this.bestValue = bestValue;
    }
}
