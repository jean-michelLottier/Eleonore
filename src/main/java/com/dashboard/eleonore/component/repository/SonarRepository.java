package com.dashboard.eleonore.component.repository;

import com.dashboard.eleonore.component.repository.entity.Sonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SonarRepository extends JpaRepository<Sonar, Long> {
    @Query("select s from Sonar s where s.id in (select cpt.elementId from Component cpt where cpt.dashboardId = :dashboardId and cpt.type = 'SONAR')")
    List<Sonar> findAllByDashboardId(@Param("dashboardId") Long dashboardId);
}
