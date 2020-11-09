package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.actuator.dto.ActuatorDTO;
import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.sonar.dto.SonarDTO;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class ElementsServiceImpl extends ElementServiceImpl implements ElementsService {

    private final Set<ElementService> elementServices;

    private final ComponentRepository componentRepository;

    @Autowired
    public ElementsServiceImpl(
            ComponentRepository componentRepository,
            @Qualifier("sonarEltServiceImpl") ElementService<SonarDTO> sonarEltService,
            @Qualifier("actuatorEltServiceImpl") ElementService<ActuatorDTO> actuatorEltService) {
        super(componentRepository);
        this.componentRepository = componentRepository;
        this.elementServices = new HashSet<>();
        this.elementServices.add(sonarEltService);
        this.elementServices.add(actuatorEltService);
    }

    @Override
    @Transactional
    public void deleteElements(Long dashboardId) {
        this.componentRepository.deleteDashboardComponents(dashboardId);

        this.elementServices.forEach(elementService -> elementService.deleteElements(dashboardId));
    }

    @Override
    public void copyDashboardElements(Long dashboardIdOriginal, Long dashboardIdCopy) {
        this.elementServices.forEach(elementService -> elementService.copyDashboardElements(dashboardIdOriginal, dashboardIdCopy));
    }

    @Override
    public List<ElementDTO> getElements(Long dashboardId) {
        List<ElementDTO> elementDTOList = new ArrayList<>();
        this.elementServices.forEach(elementService -> elementDTOList.addAll(elementService.getElements(dashboardId)));

        return elementDTOList;
    }
}
