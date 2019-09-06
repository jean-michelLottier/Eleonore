package com.dashboard.eleonore.component.repository.entity;

import com.dashboard.eleonore.component.dto.ComponentDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dashboard_id", nullable = false)
    private Long dashboardId;

    @Column(name = "element_id", nullable = false)
    private Long elementId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ElementType type;

    public Component() {
    }

    public Component(ComponentDTO componentDTO) {
        this.id = componentDTO.getId();
        this.dashboardId = componentDTO.getDashboardId();
        this.elementId = componentDTO.getElementId();
        this.type = componentDTO.getType();
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
