package com.dashboard.eleonore.element.repository.entity;

import com.dashboard.eleonore.element.dto.SonarMetricDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "sonar_metric")
public class SonarMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric", nullable = false)
    private String metric;

    @ManyToOne(targetEntity = Sonar.class)
    @JoinColumn(name = "sonar_id", updatable = false)
    private Sonar sonar;

    public SonarMetric() {
    }

    public SonarMetric(SonarMetricDTO sonarMetricDTO) {
        this.id = sonarMetricDTO.getId();
        this.metric = sonarMetricDTO.getMetric();
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

    public Sonar getSonar() {
        return sonar;
    }

    public void setSonar(Sonar sonar) {
        this.sonar = sonar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric);
    }
}
