package com.dashboard.eleonore.element.dto;

import com.dashboard.eleonore.element.repository.entity.Component;
import com.dashboard.eleonore.element.repository.entity.ElementType;

import java.io.Serializable;

public class ComponentDTO implements Serializable {

    private static final long serialVersionUID = -4856645410787944422L;

    private Long id;
    private Long dashboardId;
    private Long elementId;
    private ElementType type;

    public ComponentDTO() {
    }

    public ComponentDTO(Component component) {
        this.id = component.getId();
        this.dashboardId = component.getDashboardId();
        this.elementId = component.getElementId();
        this.type = component.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }
}
