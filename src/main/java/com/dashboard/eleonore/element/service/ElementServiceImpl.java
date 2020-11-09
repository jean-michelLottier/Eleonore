package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import com.dashboard.eleonore.element.repository.entity.Component;
import com.dashboard.eleonore.element.repository.entity.ElementType;

public abstract class ElementServiceImpl {
    private ComponentRepository componentRepository;

    public ElementServiceImpl(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public boolean isComponentEditable(Long profileId, Long componentId, ElementType type) {
        if (profileId == null || componentId == null || type == null) {
            return false;
        }

        return this.componentRepository.isComponentEditable(componentId, type, profileId);
    }

    public void saveComponent(Long dashboardId, ElementDTO savedElementDTO) {
        if (savedElementDTO != null) {
            var component = new Component();
            component.setDashboardId(dashboardId);
            component.setElementId(savedElementDTO.getId());
            component.setType(savedElementDTO.getType());
            this.componentRepository.save(component);
        }
    }
}
