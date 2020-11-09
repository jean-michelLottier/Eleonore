package com.dashboard.eleonore.element.actuator.service;

import com.dashboard.eleonore.element.actuator.dto.ActuatorDTO;
import com.dashboard.eleonore.element.actuator.dto.ActuatorMetricDTO;
import com.dashboard.eleonore.element.actuator.repository.entity.Actuator;
import com.dashboard.eleonore.element.actuator.repository.entity.ActuatorMetric;
import com.dashboard.eleonore.element.dto.*;
import com.dashboard.eleonore.element.actuator.repository.ActuatorMetricRepository;
import com.dashboard.eleonore.element.actuator.repository.ActuatorRepository;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import com.dashboard.eleonore.element.repository.entity.*;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.element.service.ElementServiceImpl;
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
public class ActuatorEltServiceImpl extends ElementServiceImpl implements ElementService<ActuatorDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorEltServiceImpl.class);

    private final ComponentRepository componentRepository;
    private final ActuatorRepository actuatorRepository;
    private final ActuatorMetricRepository actuatorMetricRepository;

    @Autowired
    public ActuatorEltServiceImpl(ComponentRepository componentRepository, ActuatorRepository actuatorRepository,
                                  ActuatorMetricRepository actuatorMetricRepository) {
        super(componentRepository);
        this.componentRepository = componentRepository;
        this.actuatorRepository = actuatorRepository;
        this.actuatorMetricRepository = actuatorMetricRepository;
    }

    @Override
    public Optional<ActuatorDTO> getElement(Long profileId, Long elementId) {
        if (profileId == null || elementId == null) {
            return Optional.empty();
        }

        ActuatorDTO elementDTO = null;

        Optional<Actuator> optionalActuator = this.actuatorRepository.find(profileId, elementId);
        if (optionalActuator.isPresent()) {
            elementDTO = new ActuatorDTO(optionalActuator.get());
        }

        return Optional.ofNullable(elementDTO);
    }

    @Override
    public Optional<ActuatorDTO> saveElement(Long dashboardId, ActuatorDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return Optional.empty();
        }

        LOGGER.info("eleonore - Adding actuator element for dashboard {}", dashboardId);
        Actuator actuator = this.actuatorRepository.save(new Actuator(elementDTO));
        ActuatorDTO savedElementDTO = new ActuatorDTO(actuator);

        // If the saved element is new then it is linked with the given dashboard (defined by its id)
        if (elementDTO.getId() == null) {
            this.saveComponent(dashboardId, savedElementDTO);
        }

        return Optional.of(savedElementDTO);
    }

    @Override
    @Transactional
    public void deleteElement(Long dashboardId, ActuatorDTO elementDTO) {
        var components = this.componentRepository.findDashboardComponentsByType(dashboardId, elementDTO.getType());

        LOGGER.info("eleonore - Removing {} actuator element(s) for dashboard {}", components.size(), dashboardId);
        components.stream().filter(component -> ElementType.ACTUATOR.equals(component.getType()))
                .forEach(component -> this.actuatorRepository.deleteById(component.getElementId()));
    }

    @Override
    @Transactional
    public void deleteElement(Long profileId, Long dashboardId, Long elementId) {
        if (profileId == null || dashboardId == null || elementId == null) {
            return;
        }

        // Step 1: delete the element in database
        LOGGER.info("eleonore - Removing actuator element {}", elementId);
        this.actuatorMetricRepository.delete(profileId, dashboardId, elementId);
        this.actuatorRepository.delete(profileId, dashboardId, elementId);

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementId, ElementType.ACTUATOR);
    }

    @Override
    @Transactional
    public Optional<ActuatorDTO> updateElement(ActuatorDTO elementDTO) {
        if (elementDTO == null) {
            return Optional.empty();
        }

        LOGGER.info("eleonore - Updating actuator element {}", elementDTO.getId());
        Actuator actuator = new Actuator(elementDTO);
        // Step 1: Delete all unselected metrics
        List<ActuatorMetricDTO> currentMetrics = this.actuatorMetricRepository.findById(
                actuator.getId()).stream().map(ActuatorMetricDTO::new).collect(Collectors.toList());
        List<ActuatorMetricDTO> currentMetricsToDelete = currentMetrics.stream()
                .filter(currentMetric -> actuator.getMetrics().stream()
                        .noneMatch(selectedMetric -> selectedMetric.getMetric().equals(currentMetric.getMetric())))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(currentMetricsToDelete)) {
            this.actuatorMetricRepository.deleteAll(
                    currentMetricsToDelete.stream().map(ActuatorMetric::new).collect(Collectors.toList())
            );
        }

        // Step 2: Save metrics
        List<ActuatorMetric> updatedMetrics = this.actuatorMetricRepository.saveAll(actuator.getMetrics());
        actuator.getMetrics().stream()
                .filter(metric -> metric.getId() == null)
                .forEach(metric ->
                        metric.setId(updatedMetrics.stream().filter(updatedMetric ->
                                updatedMetric.getMetric().equals(metric.getMetric())).findFirst().get().getId()
                        )
                );

        // Step 3: Save element
        elementDTO = new ActuatorDTO(this.actuatorRepository.save(actuator));

        return Optional.of(elementDTO);
    }

    @Override
    @Transactional
    public void deleteElements(Long dashboardId) {
        var components = this.componentRepository.findDashboardComponentsByType(dashboardId, ElementType.ACTUATOR);

        LOGGER.info("eleonore - Removing {} actuator element(s) for dashboard {}", components.size(), dashboardId);
        components.forEach(component -> this.actuatorRepository.deleteById(component.getElementId()));
    }

    @Override
    public void copyDashboardElements(Long dashboardIdOriginal, Long dashboardIdCopy) {
        if (dashboardIdOriginal == null || dashboardIdCopy == null) {
            return;
        }

        LOGGER.info("eleonore - Copying actuator elements from dashboard {} to dashboard {}", dashboardIdOriginal, dashboardIdCopy);
        List<Actuator> actuatorOriginalList = this.actuatorRepository.findAllByDashboardId(dashboardIdOriginal);

        actuatorOriginalList.stream().map(ActuatorDTO::new).forEach(actuatorDTO -> {
            actuatorDTO.setId(null);
            Actuator actuatorCopy = this.actuatorRepository.save(new Actuator(actuatorDTO));
            Component component = new Component();
            component.setDashboardId(dashboardIdCopy);
            component.setElementId(actuatorCopy.getId());
            component.setType(ElementType.ACTUATOR);
            this.componentRepository.save(component);
        });
    }

    @Override
    public List<ElementDTO> getElements(Long dashboardId) {
        return this.actuatorRepository.findAllByDashboardId(dashboardId)
                .stream()
                .map(ActuatorDTO::new)
                .collect(Collectors.toList());
    }
}
