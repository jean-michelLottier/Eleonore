package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.dto.SonarDTO;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import com.dashboard.eleonore.element.repository.SonarMetricRepository;
import com.dashboard.eleonore.element.repository.SonarRepository;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.repository.entity.Sonar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElementServiceImpl implements ElementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementServiceImpl.class);

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private SonarRepository sonarRepository;

    @Autowired
    private SonarMetricRepository sonarMetricRepository;

    @Override
    @Transactional
    public void deleteElements(Long dashboardId) {
        var components = this.componentRepository.findAllDashboardComponents(dashboardId);
        this.componentRepository.deleteDashboardComponents(dashboardId);

        LOGGER.info("eleonore - Removing {} sonar element(s) for dashboard {}", components.size(), dashboardId);
        components.stream().filter(component -> ElementType.SONAR.equals(component.getType()))
                .forEach(component -> this.sonarRepository.deleteById(component.getElementId()));
    }

    @Override
    public void copyDashboardElements(Long dashboardIdOriginal, Long dashboardIdCopy) {
        if (dashboardIdOriginal == null || dashboardIdCopy == null) {
            return;
        }

        LOGGER.info("eleonore - Copying Sonar elements from dashboard {} to dashboard {}", dashboardIdOriginal, dashboardIdCopy);
        List<Sonar> sonarOriginalList = this.sonarRepository.findAllByDashboardId(dashboardIdOriginal);

        sonarOriginalList.stream().map(SonarDTO::new).forEach(sonarDTO -> {
            sonarDTO.setId(null);
            Sonar sonarCopy = this.sonarRepository.save(new Sonar(sonarDTO));
            com.dashboard.eleonore.element.repository.entity.Component component = new com.dashboard.eleonore.element.repository.entity.Component();
            component.setDashboardId(dashboardIdCopy);
            component.setElementId(sonarCopy.getId());
            component.setType(ElementType.SONAR);
            this.componentRepository.save(component);
        });
    }

    @Override
    public List<ElementDTO> getElements(Long dashboardId) {
        var elementDTOList = new ArrayList<ElementDTO>();
        elementDTOList.addAll(this.sonarRepository.findAllByDashboardId(dashboardId)
                .stream()
                .map(SonarDTO::new)
                .collect(Collectors.toList()));

        return elementDTOList;
    }

    @Override
    public ElementDTO saveElement(Long dashboardId, ElementDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return null;
        }

        ElementDTO savedElementDTO = null;
        switch (elementDTO.getType()) {
            case SONAR:
                LOGGER.info("eleonore - Adding sonar element for dashboard {}", dashboardId);
                Sonar sonar = this.sonarRepository.save(new Sonar((SonarDTO) elementDTO));
                savedElementDTO = new SonarDTO(sonar);
                break;
            default:
        }

        // If the saved element is new then it is linked with the given dashboard (defined by its id)
        if (elementDTO.getId() == null && savedElementDTO != null) {
            var component = new com.dashboard.eleonore.element.repository.entity.Component();
            component.setDashboardId(dashboardId);
            component.setElementId(savedElementDTO.getId());
            component.setType(savedElementDTO.getType());
            this.componentRepository.save(component);
        }

        return savedElementDTO;
    }

    @Override
    @Transactional
    public void deleteElement(Long dashboardId, ElementDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return;
        }

        // Step 1: delete the element in database
        switch (elementDTO.getType()) {
            case SONAR:
                LOGGER.info("eleonore - Removing sonar element {}", elementDTO.getId());
                this.sonarRepository.delete(new Sonar((SonarDTO) elementDTO));
                break;
            default:
        }

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementDTO.getId(), elementDTO.getType());
    }

    @Override
    @Transactional
    public void deleteElement(Long profileId, Long dashboardId, Long elementId, ElementType elementType) {
        if(profileId == null || dashboardId == null || elementId == null || elementType == null) {
            return;
        }

        // Step 1: delete the element in database
        switch (elementType) {
            case SONAR:
                LOGGER.info("eleonore - Removing sonar element {}", elementId);
                this.sonarMetricRepository.deleteSonarMetrics(profileId, dashboardId, elementId);
                this.sonarRepository.delete(profileId, dashboardId, elementId);
                break;
            default:
        }

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementId, elementType);
    }
}
