package com.dashboard.eleonore.element.dto;

import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.repository.entity.Sonar;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class SonarDTO extends ElementDTO implements Serializable {

    private static final long serialVersionUID = 483528404395832976L;

    private String url;
    private String projectName;
    private String projectKey;
    private Set<SonarMetricDTO> sonarMetrics;

    public SonarDTO() {
        super(null);
    }

    public SonarDTO(Sonar sonar) {
        super(sonar.getId());
        this.url = sonar.getUrl();
        this.projectName = sonar.getProjectName();
        this.projectKey = sonar.getProjectKey();
        if (!CollectionUtils.isEmpty(sonar.getMetrics())) {
            this.sonarMetrics = sonar.getMetrics().stream().map(SonarMetricDTO::new).collect(Collectors.toSet());
        }
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

    public Set<SonarMetricDTO> getSonarMetrics() {
        return sonarMetrics;
    }

    public void setSonarMetrics(Set<SonarMetricDTO> sonarMetrics) {
        this.sonarMetrics = sonarMetrics;
    }

    @Override
    public ElementType getType() {
        return ElementType.SONAR;
    }
}
