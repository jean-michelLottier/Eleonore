package com.dashboard.eleonore.element.sonar.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.repository.ComponentRepository;
import com.dashboard.eleonore.element.repository.entity.Component;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.element.service.ElementServiceImpl;
import com.dashboard.eleonore.element.sonar.dto.SonarDTO;
import com.dashboard.eleonore.element.sonar.dto.SonarMetricDTO;
import com.dashboard.eleonore.element.sonar.mapper.SonarMapper;
import com.dashboard.eleonore.element.sonar.mapper.SonarMetricMapper;
import com.dashboard.eleonore.element.sonar.repository.SonarMetricRepository;
import com.dashboard.eleonore.element.sonar.repository.SonarRepository;
import com.dashboard.eleonore.element.sonar.repository.entity.Sonar;
import com.dashboard.eleonore.element.sonar.repository.entity.SonarMetric;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SonarEltServiceImpl extends ElementServiceImpl implements ElementService<SonarDTO> {
    private final SonarRepository sonarRepository;
    private final SonarMetricRepository sonarMetricRepository;
    private final ComponentRepository componentRepository;
    private final SonarMetricMapper sonarMetricMapper;
    private final SonarMapper sonarMapper;

    @Autowired
    public SonarEltServiceImpl(ComponentRepository componentRepository, SonarRepository sonarRepository,
                               SonarMetricRepository sonarMetricRepository) {
        super(componentRepository);

        this.componentRepository = componentRepository;
        this.sonarRepository = sonarRepository;
        this.sonarMetricRepository = sonarMetricRepository;
        this.sonarMetricMapper = Mappers.getMapper(SonarMetricMapper.class);
        this.sonarMapper = Mappers.getMapper(SonarMapper.class);
    }

    @Override
    @Transactional
    public void deleteElements(Long dashboardId) {
        var components = this.componentRepository.findAllDashboardComponents(dashboardId);

        log.info("eleonore - Removing {} sonar element(s) for dashboard {}", components.size(), dashboardId);
        components.stream().filter(component -> ElementType.SONAR.equals(component.getType()))
                .forEach(component -> this.sonarRepository.deleteById(component.getElementId()));
    }

    @Override
    public void copyDashboardElements(Long dashboardIdOriginal, Long dashboardIdCopy) {
        if (dashboardIdOriginal == null || dashboardIdCopy == null) {
            return;
        }

        log.info("eleonore - Copying Sonar elements from dashboard {} to dashboard {}", dashboardIdOriginal, dashboardIdCopy);
        List<Sonar> sonarOriginalList = this.sonarRepository.findAllByDashboardId(dashboardIdOriginal);

        sonarOriginalList.stream().map(this.sonarMapper::sonarToSonarDTO).forEach(sonarDTO -> {
            sonarDTO.setId(null);
            Sonar sonarCopy = this.sonarRepository.save(this.sonarMapper.sonarDTOToSonar(sonarDTO));
            Component component = new Component();
            component.setDashboardId(dashboardIdCopy);
            component.setElementId(sonarCopy.getId());
            component.setType(ElementType.SONAR);
            this.componentRepository.save(component);
        });
    }

    @Override
    public Optional<SonarDTO> getElement(Long profileId, Long elementId) {
        if (profileId == null || elementId == null) {
            return Optional.empty();
        }

        SonarDTO elementDTO = null;

        Optional<Sonar> optionalSonar = this.sonarRepository.find(profileId, elementId);
        if (optionalSonar.isPresent()) {
            elementDTO = this.sonarMapper.sonarToSonarDTO(optionalSonar.get());
        }

        return Optional.ofNullable(elementDTO);
    }

    @Override
    public List<ElementDTO> getElements(Long dashboardId) {
        return this.sonarRepository.findAllByDashboardId(dashboardId)
                .stream()
                .map(this.sonarMapper::sonarToSonarDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SonarDTO> saveElement(Long dashboardId, SonarDTO elementDTO) {
        if (dashboardId == null || elementDTO == null) {
            return Optional.empty();
        }

        log.info("eleonore - Adding sonar element for dashboard {}", dashboardId);
        Sonar sonar = this.sonarRepository.save(this.sonarMapper.sonarDTOToSonar(elementDTO));
        SonarDTO savedElementDTO = this.sonarMapper.sonarToSonarDTO(sonar);

        // If the saved element is new then it is linked with the given dashboard (defined by its id)
        if (elementDTO.getId() == null) {
            this.saveComponent(dashboardId, savedElementDTO);
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
        log.info("eleonore - Removing sonar element {}", elementDTO.getId());
        this.sonarRepository.delete(this.sonarMapper.sonarDTOToSonar(elementDTO));

        // Step 2: delete the component in database
        this.componentRepository.deleteDashboardComponent(dashboardId, elementDTO.getId(), elementDTO.getType());
    }

    @Override
    @Transactional
    public void deleteElement(Long profileId, Long dashboardId, Long elementId) {
        if (profileId == null || dashboardId == null || elementId == null) {
            return;
        }

        // Step 1: delete the element in database
        log.info("eleonore - Removing sonar element {}", elementId);
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

        log.info("eleonore - Updating sonar element {}", elementDTO.getId());
        Sonar sonar = this.sonarMapper.sonarDTOToSonar(elementDTO);
        // Step 1: Delete all unselected metrics
        List<SonarMetricDTO> currentMetrics = this.sonarMetricRepository.findSonarMetricsBySonarId(
                sonar.getId()).stream().map(this.sonarMetricMapper::sonarMetricToSonarMetricDTO).collect(Collectors.toList());
        List<SonarMetricDTO> currentMetricsToDelete = currentMetrics.stream()
                .filter(currentMetric -> sonar.getMetrics().stream()
                        .noneMatch(selectedMetric -> selectedMetric.getMetric().equals(currentMetric.getMetric())))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(currentMetricsToDelete)) {
            this.sonarMetricRepository.deleteAll(
                    currentMetricsToDelete.stream().map(this.sonarMetricMapper::sonarMetricDTOToSonarMetric)
                            .collect(Collectors.toList())
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
        elementDTO = this.sonarMapper.sonarToSonarDTO(this.sonarRepository.save(sonar));

        return Optional.of(elementDTO);
    }
}
