package com.dashboard.eleonore.element.dto;

import com.dashboard.eleonore.element.repository.entity.ElementType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ComponentDTO implements Serializable {

    private static final long serialVersionUID = -4856645410787944422L;

    private Long id;
    private Long dashboardId;
    private Long elementId;
    private ElementType type;
}
