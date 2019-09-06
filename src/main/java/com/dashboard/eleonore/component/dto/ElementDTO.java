package com.dashboard.eleonore.component.dto;

import com.dashboard.eleonore.component.repository.entity.ElementType;

import java.io.Serializable;

public abstract class ElementDTO implements Serializable {

    private static final long serialVersionUID = -6072357671297457056L;

    private Long id;

    public ElementDTO(Long id) {
        this.id = id;
    }

    public abstract ElementType getType();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
