package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.repository.ComponentRepository;
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
}
