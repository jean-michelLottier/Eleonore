package com.dashboard.eleonore.element.dto;

import com.dashboard.eleonore.element.repository.entity.ElementType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public abstract class ElementDTO implements Serializable {

    private static final long serialVersionUID = -6072357671297457056L;

    private Long id;

    public ElementDTO(Long id) {
        this.id = id;
    }

    public abstract ElementType getType();
}
