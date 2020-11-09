package com.dashboard.eleonore.element.actuator.repository.entity;

import com.dashboard.eleonore.element.actuator.dto.ActuatorMetricDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "actuator_metric")
public class ActuatorMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric", nullable = false)
    private String metric;

    @ManyToOne(targetEntity = Actuator.class)
    @JoinColumn(name = "actuator_id", updatable = false)
    private Actuator actuator;

    public ActuatorMetric() {
    }

    public ActuatorMetric(ActuatorMetricDTO metricDTO) {
        this.id = metricDTO.getId();
        this.metric = metricDTO.getMetric();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActuatorMetric that = (ActuatorMetric) o;
        return id.equals(that.id) &&
                metric.equals(that.metric) &&
                Objects.equals(actuator, that.actuator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metric);
    }
}
