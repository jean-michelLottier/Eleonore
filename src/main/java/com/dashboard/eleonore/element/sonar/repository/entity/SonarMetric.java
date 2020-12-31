package com.dashboard.eleonore.element.sonar.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
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

    @Override
    public int hashCode() {
        return Objects.hash(metric);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof SonarMetric) {
            SonarMetric sonarMetric = (SonarMetric) obj;
            return this.id == sonarMetric.id
                    && (this.metric != null && this.metric.equals(sonarMetric.metric)
                    || this.metric == null && sonarMetric.metric == null)
                    && ((this.sonar != null && this.sonar.equals(sonarMetric.sonar))
                    || this.sonar == null && sonarMetric.sonar == null);
        }

        return false;
    }
}
