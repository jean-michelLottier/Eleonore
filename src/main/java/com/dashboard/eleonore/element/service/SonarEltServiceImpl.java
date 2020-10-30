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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SonarEltServiceImpl extends ElementServiceImpl implements ElementService<SonarDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarEltServiceImpl.class);

    @Autowired
    private SonarRepository sonarRepository;

    @Autowired
    private SonarMetricRepository sonarMetricRepository;

    private ComponentRepository componentRepository;

    @Autowired
    public SonarEltServiceImpl(ComponentRepository componentRepository) {
        super(componentRepository);

        this.componentRepository = componentRepository;
    }

    @Override
    @Transactional
    public void deleteElements(Long dashboardId) {
        var components = this.componentRepository.findAllDashboardComponents(dashboardId);

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
    public Optional<SonarDTO> getElement(Long profileId, Long elementId, Class<SonarDTO> type) {
        if (profileId == null || elementId == null || type == null) {
            return Optional.empty();
        }

        SonarDTO elementDTO = null;

        Optional<Sonar> optionalSonar = this.sonarRepository.find(profileId, elementId);
        if (optionalSonar.isPresent()) {
            elementDTO = new SonarDTO(optionalSonar.get());
        }

        return Optional.ofNullable(elementDTO);
    }

    @Override
    public List<ElementDTO> getElements(Long dashboardId) {
        return this.sonarRepository.findAllByDashboardId(dashboardId)
                .stream()
                .map(SonarDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SonarDTO> saveElement(Long dashboardId, SonarDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return Optional.empty();
        }

        LOGGER.info("eleonore - Adding sonar element for dashboard {}", dashboardId);
        Sonar sonar = this.sonarRepository.save(new Sonar((SonarDTO) elementDTO));
        SonarDTO savedElementDTO = new SonarDTO(sonar);

        // If the saved element is new then it is linked with the given dashboard (defined by its id)
        if (elementDTO.getId() == null && savedElementDTO != null) {
            var component = new com.dashboard.eleonore.element.repository.entity.Component();
            component.setDashboardId(dashboardId);
            component.setElementId(savedElementDTO.getId());
            component.setType(savedElementDTO.getType());
            this.componentRepository.save(component);
        }

        return Optional.ofNullable(savedElementDTO);
    }

    @Override
    @Transactional
    public void deleteElement(Long dashboardId, SonarDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return;
        }

        // Step 1: delete the element in database
        LOGGER.info("eleonore - Removing sonar element {}", elementDTO.getId());
        this.sonarRepository.delete(new Sonar(elementDTO));

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementDTO.getId(), elementDTO.getType());
    }

    @Override
    @Transactional
    public void deleteElement(Long profileId, Long dashboardId, Long elementId, Class<SonarDTO> elementType) {
        if (profileId == null || dashboardId == null || elementId == null || elementType == null) {
            return;
        }

        // Step 1: delete the element in database
        LOGGER.info("eleonore - Removing sonar element {}", elementId);
        this.sonarMetricRepository.deleteSonarMetrics(profileId, dashboardId, elementId);
        this.sonarRepository.delete(profileId, dashboardId, elementId);

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementId, ElementType.SONAR);
    }

    @Override
    @Transactional
    public Optional<SonarDTO> updateElement(SonarDTO elementDTO) {
        if (elementDTO == null) {
            return Optional.empty();
        }


        LOGGER.info("eleonore - Updating sonar element {}", elementDTO.getId());
        Sonar sonar = new Sonar(elementDTO);
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
        List<SonarMetric> updatedMetrics = this.sonarMetricRepository.saveAll(sonar.getMetrics());
        sonar.getMetrics().stream()
                .filter(metric -> metric.getId() == null)
                .forEach(metric ->
                        metric.setId(updatedMetrics.stream().filter(updatedMetric ->
                                updatedMetric.getMetric().equals(metric.getMetric())).findFirst().get().getId()
                        )
                );
        // Step 3: Save sonar
        elementDTO = new SonarDTO(this.sonarRepository.save(sonar));

        return Optional.of(elementDTO);
    }
}
