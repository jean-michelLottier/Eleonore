package com.dashboard.eleonore.sonar.service;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonRootName(value = "component")
public class SonarOT {
    private String id;
    private String key;
    private String name;
    private String description;
    private String qualifier;
    private List<SonarMetricOT> metrics;

    public List<SonarMetricOT> getMetrics() {
        return metrics;
    }

    @JsonSetter("measures")
    public void setMetrics(List<SonarMetricOT> metrics) {
        this.metrics = metrics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
