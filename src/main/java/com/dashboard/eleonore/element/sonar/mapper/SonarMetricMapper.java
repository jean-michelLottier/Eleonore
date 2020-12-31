package com.dashboard.eleonore.element.sonar.mapper;

import com.dashboard.eleonore.element.sonar.dto.SonarMetricDTO;
import com.dashboard.eleonore.element.sonar.repository.entity.SonarMetric;
import org.mapstruct.Mapper;

@Mapper
public interface SonarMetricMapper {
    SonarMetricDTO sonarMetricToSonarMetricDTO(SonarMetric sonarMetric);

    SonarMetric sonarMetricDTOToSonarMetric(SonarMetricDTO sonarMetricDTO);
}
