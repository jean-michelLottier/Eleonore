package com.dashboard.eleonore.element.repository.entity;

import com.dashboard.eleonore.element.dto.SonarDTO;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
public class Sonar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @URL
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_key")
    private String projectKey;

    @OneToMany(mappedBy = "sonar", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SonarMetric> metrics;

    public Sonar() {
    }

    public Sonar(SonarDTO sonarDTO) {
        this.id = sonarDTO.getId();
        this.url = sonarDTO.getUrl();
        this.projectName = sonarDTO.getProjectName();
        this.projectKey = sonarDTO.getProjectKey();
        if (!CollectionUtils.isEmpty(sonarDTO.getSonarMetrics())) {
            this.metrics = sonarDTO.getSonarMetrics().stream().map(SonarMetric::new).collect(Collectors.toSet());
            this.metrics.forEach(sonarMetric -> sonarMetric.setSonar(this));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public Set<SonarMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Set<SonarMetric> metrics) {
        this.metrics = metrics;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, projectName, projectKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Sonar) {
            Sonar sonar = (Sonar) obj;
            return sonar.id == this.id
                    && this.url.equals(sonar.url)
                    && ((this.projectKey != null && this.projectKey.equals(sonar.projectKey))
                    || this.projectKey == null && sonar.projectKey == null)
                    && ((this.projectName != null && this.projectName.equals(sonar.projectName))
                    || this.projectName == null && sonar.projectName == null)
                    && ((this.metrics != null && this.metrics.equals(sonar.metrics))
                    || this.metrics == null && sonar.metrics == null);
        }

        return false;
    }
}
