package com.dashboard.eleonore.element.repository;

import com.dashboard.eleonore.element.repository.entity.Component;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Modifying
    @Query("delete from Component cpt where cpt.dashboardId = :dashboardId")
    void deleteDashboardComponents(@Param("dashboardId") Long dashboardId);

    @Modifying
    @Query("delete from Component cpt where cpt.dashboardId = :dashboardId and cpt.elementId = :elementId and cpt.type = :type")
    void deleteDashboardComponent(@Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId, @Param("type") ElementType type);

    @Query("select cpt from Component cpt where cpt.dashboardId = :dashboardId order by cpt.type")
    List<Component> findAllDashboardComponents(@Param("dashboardId") Long dashboardId);
}