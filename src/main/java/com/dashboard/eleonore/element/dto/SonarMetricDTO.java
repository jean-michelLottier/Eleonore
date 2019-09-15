package com.dashboard.eleonore.element.dto;

import com.dashboard.eleonore.element.repository.entity.SonarMetric;

import java.io.Serializable;

public class SonarMetricDTO implements Serializable {

    private static final long serialVersionUID = 3678203658045976600L;

    private Long id;
    private String metric;

    public SonarMetricDTO() {
    }

    public SonarMetricDTO(SonarMetric sonarMetric) {
        this.id = sonarMetric.getId();
        this.metric = sonarMetric.getMetric();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }
}
