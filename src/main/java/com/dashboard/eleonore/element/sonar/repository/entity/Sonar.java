package com.dashboard.eleonore.element.sonar.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
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
