package com.dashboard.eleonore.component.service;

import com.dashboard.eleonore.component.dto.ElementDTO;
import com.dashboard.eleonore.component.dto.SonarDTO;
import com.dashboard.eleonore.component.repository.ComponentRepository;
import com.dashboard.eleonore.component.repository.SonarRepository;
import com.dashboard.eleonore.component.repository.entity.ElementType;
import com.dashboard.eleonore.component.repository.entity.Sonar;
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
            com.dashboard.eleonore.component.repository.entity.Component component = new com.dashboard.eleonore.component.repository.entity.Component();
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
}
