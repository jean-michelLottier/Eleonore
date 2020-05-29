package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.dto.SonarDTO;
import com.dashboard.eleonore.element.dto.SonarMetricDTO;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import com.dashboard.eleonore.element.repository.SonarMetricRepository;
import com.dashboard.eleonore.element.repository.SonarRepository;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.repository.entity.Sonar;
import com.dashboard.eleonore.element.repository.entity.SonarMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Optional<ElementDTO> getElement(Long profileId, Long elementId, ElementType type) {
        if (profileId == null || elementId == null || type == null) {
            return Optional.empty();
        }

        ElementDTO elementDTO = null;
        switch (type) {
            case SONAR:
                Optional<Sonar> optionalSonar = this.sonarRepository.find(profileId, elementId);
                if (optionalSonar.isPresent()) {
                    elementDTO = new SonarDTO(optionalSonar.get());
                }
                break;
            default:
        }

        return Optional.ofNullable(elementDTO);
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
        if (profileId == null || dashboardId == null || elementId == null || elementType == null) {
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

    @Override
    @Transactional
    public void updateElement(ElementDTO elementDTO) {
        if (elementDTO == null) {
            return;
        }

        switch (elementDTO.getType()) {
            case SONAR:
                LOGGER.info("eleonore - Updating sonar element {}", elementDTO.getId());
                Sonar sonar = new Sonar((SonarDTO) elementDTO);
                // Step 1: Delete all unselected metrics
                List<SonarMetricDTO> currentMetrics = this.sonarMetricRepository.findSonarMetricsBySonarId(
                        sonar.getId()).stream().map(SonarMetricDTO::new).collect(Collectors.toList());
                List<SonarMetricDTO> currentMetricsToDelete = currentMetrics.stream()
                        .filter(currentMetric -> sonar.getMetrics().stream()
                                .noneMatch(selectedMetric -> selectedMetric.getMetric().equals(currentMetric.getMetric())))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(currentMetricsToDelete)) {
                    this.sonarMetricRepository.deleteAll(
                            currentMetricsToDelete.stream().map(SonarMetric::new).collect(Collectors.toList())
                    );
                }

                // Step 2: Save metrics
                this.sonarMetricRepository.saveAll(sonar.getMetrics());
                // Step 3: Save sonar
                this.sonarRepository.save(sonar);
                break;
            default:
        }
    }

    @Override
    public boolean isComponentEditable(Long profileId, Long componentId, ElementType type) {
        if (profileId == null || componentId == null || type == null) {
            return false;
        }

        return this.componentRepository.isComponentEditable(componentId, type, profileId);
    }
}
