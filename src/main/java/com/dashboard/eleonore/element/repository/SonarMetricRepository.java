package com.dashboard.eleonore.element.repository;

import com.dashboard.eleonore.element.repository.entity.SonarMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SonarMetricRepository extends JpaRepository<SonarMetric, Long> {
    @Modifying
    @Query("delete from SonarMetric s where s.sonar = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId and c.dashboardId = (\n" +
            "select cst.dashboardId from Customer cst where cst.profileId = :profileId and cst.dashboardId = :dashboardId and cst.editable=1))")
    void deleteSonarMetrics(@Param("profileId") Long profileId, @Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId);
}