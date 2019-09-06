package com.dashboard.eleonore.dashboard.dto;

import com.dashboard.eleonore.component.dto.ElementDTO;
import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardDTO {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<ElementDTO> elements;

    public DashboardDTO() {
    }

    public DashboardDTO(Dashboard dashboard) {
        this.id = dashboard.getId();
        this.name = dashboard.getName();
        this.createdDate = dashboard.getCreatedDate();
        this.modifiedDate = dashboard.getModifiedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<ElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<ElementDTO> elements) {
        this.elements = elements;
    }
}
