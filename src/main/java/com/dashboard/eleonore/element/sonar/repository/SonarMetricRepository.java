package com.dashboard.eleonore.element.sonar.repository;

import com.dashboard.eleonore.element.sonar.repository.entity.SonarMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SonarMetricRepository extends JpaRepository<SonarMetric, Long> {
    @Modifying
    @Query("delete from SonarMetric s where s.sonar.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.SONAR " +
            "and c.dashboardId = (select cst.dashboardId from Customer cst where cst.profileId = :profileId and cst.dashboardId = :dashboardId and cst.editable=true))")
    void deleteSonarMetrics(@Param("profileId") Long profileId, @Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId);

    @Query("select sm from SonarMetric sm where sm.sonar.id = :sonarId")
    List<SonarMetric> findSonarMetricsBySonarId(@Param("sonarId") long sonarId);
}
