package com.dashboard.eleonore.element.actuator.repository.entity;

import com.dashboard.eleonore.element.actuator.dto.ActuatorDTO;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
public class Actuator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @URL
    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "actuator", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ActuatorMetric> metrics;

    public Actuator() {}

    public Actuator(ActuatorDTO actuatorDTO) {
        this.id = actuatorDTO.getId();
        this.url = actuatorDTO.getUrl();
        if (!CollectionUtils.isEmpty(actuatorDTO.getActuatorMetrics())) {
            this.metrics = actuatorDTO.getActuatorMetrics().stream().map(ActuatorMetric::new).collect(Collectors.toSet());
            this.metrics.forEach(actuatorMetric -> actuatorMetric.setActuator(this));
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<ActuatorMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Set<ActuatorMetric> metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actuator actuator = (Actuator) o;
        return id.equals(actuator.id) &&
                url.equals(actuator.url) &&
                Objects.equals(metrics, actuator.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
