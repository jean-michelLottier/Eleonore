package com.dashboard.eleonore.element.sonar.mapper;

import com.dashboard.eleonore.element.sonar.dto.SonarDTO;
import com.dashboard.eleonore.element.sonar.repository.entity.Sonar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Mapper
public interface SonarMapper {
    default SonarDTO sonarToSonarDTO(Sonar sonar) {
        if (sonar == null) {
            return null;
        }

        SonarDTO sonarDTO = new SonarDTO();
        sonarDTO.setId(sonar.getId());
        sonarDTO.setUrl(sonar.getUrl());
        sonarDTO.setProjectName(sonar.getProjectName());
        sonarDTO.setProjectKey(sonar.getProjectKey());
        if (!CollectionUtils.isEmpty(sonar.getMetrics())) {
            SonarMetricMapper sonarMetricMapper = Mappers.getMapper(SonarMetricMapper.class);
            sonarDTO.setSonarMetrics(sonar.getMetrics().stream()
                    .map(sonarMetricMapper::sonarMetricToSonarMetricDTO)
                    .collect(Collectors.toSet()));
        }

        return sonarDTO;
    }

    default Sonar sonarDTOToSonar(SonarDTO sonarDTO) {
        if (sonarDTO == null) {
            return null;
        }

        Sonar sonar = new Sonar();
        sonar.setId(sonarDTO.getId());
        sonar.setUrl(sonarDTO.getUrl());
        sonar.setProjectName(sonarDTO.getProjectName());
        sonar.setProjectKey(sonarDTO.getProjectKey());
        if (!CollectionUtils.isEmpty(sonarDTO.getSonarMetrics())) {
            SonarMetricMapper sonarMetricMapper = Mappers.getMapper(SonarMetricMapper.class);
            sonar.setMetrics(sonarDTO.getSonarMetrics().stream()
                    .map(sonarMetricMapper::sonarMetricDTOToSonarMetric)
                    .collect(Collectors.toSet()));
            sonar.getMetrics().forEach(sonarMetric -> sonarMetric.setSonar(sonar));
        }

        return sonar;
    }
}
