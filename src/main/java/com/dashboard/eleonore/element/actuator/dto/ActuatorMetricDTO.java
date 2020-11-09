package com.dashboard.eleonore.element.actuator.dto;

import com.dashboard.eleonore.element.actuator.repository.entity.ActuatorMetric;

import java.io.Serializable;

public class ActuatorMetricDTO implements Serializable {
    private static final long serialVersionUID = -7144211201603264249L;

    private Long id;
    private String metric;

    public ActuatorMetricDTO() {}

    public ActuatorMetricDTO(ActuatorMetric actuatorMetric) {
        this.id = actuatorMetric.getId();
        this.metric = actuatorMetric.getMetric();
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
