package com.dashboard.eleonore.element.mapper;

import com.dashboard.eleonore.element.dto.ComponentDTO;
import com.dashboard.eleonore.element.repository.entity.Component;
import org.mapstruct.Mapper;

@Mapper
public interface ComponentMapper {
    ComponentDTO componentToComponentDTO(Component component);

    Component componentDTOToComponent(ComponentDTO componentDTO);
}
