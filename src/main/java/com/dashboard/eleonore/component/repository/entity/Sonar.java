package com.dashboard.eleonore.component.repository.entity;

import com.dashboard.eleonore.component.dto.SonarDTO;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

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

    public Sonar() {
    }

    public Sonar(SonarDTO sonarDTO) {
        this.id = sonarDTO.getId();
        this.url = sonarDTO.getUrl();
        this.projectName = sonarDTO.getProjectName();
        this.projectKey = sonarDTO.getProjectKey();
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
}
