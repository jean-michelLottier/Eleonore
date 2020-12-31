package com.dashboard.eleonore.element.sonar.repository;

import com.dashboard.eleonore.element.sonar.repository.entity.Sonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SonarRepository extends JpaRepository<Sonar, Long> {
    @Query("select s from Sonar s where s.id in (select cpt.elementId from Component cpt where cpt.dashboardId = :dashboardId " +
            "and cpt.type = com.dashboard.eleonore.element.repository.entity.ElementType.SONAR)")
    List<Sonar> findAllByDashboardId(@Param("dashboardId") Long dashboardId);

    @Modifying
    @Query("delete from Sonar s where s.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.SONAR " +
            "and c.dashboardId = (select cst.dashboardId from Customer cst where cst.profileId = :profileId and cst.dashboardId = :dashboardId and cst.editable=true))")
    void delete(@Param("profileId") Long profileId, @Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId);

    @Query("select s from Sonar s where s.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.SONAR " +
            "and c.dashboardId in (select cst.dashboardId from Customer cst where cst.profileId = :profileId))")
    Optional<Sonar> find(@Param("profileId") Long profileId, @Param("elementId") Long elementId);
}
