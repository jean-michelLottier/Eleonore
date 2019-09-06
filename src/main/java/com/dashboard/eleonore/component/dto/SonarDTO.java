package com.dashboard.eleonore.component.dto;

import com.dashboard.eleonore.component.repository.entity.ElementType;
import com.dashboard.eleonore.component.repository.entity.Sonar;

import java.io.Serializable;

public class SonarDTO extends ElementDTO implements Serializable {

    private static final long serialVersionUID = 483528404395832976L;

    private String url;
    private String projectName;
    private String projectKey;

    public SonarDTO() {
        super(null);
    }

    public SonarDTO(Sonar sonar) {
        super(sonar.getId());
        this.url = sonar.getUrl();
        this.projectName = sonar.getProjectName();
        this.projectKey = sonar.getProjectKey();
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

    @Override
    public ElementType getType() {
        return ElementType.SONAR;
    }
}
