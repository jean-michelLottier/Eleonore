package com.dashboard.eleonore.component.repository;

import com.dashboard.eleonore.component.repository.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Modifying
    @Query("delete from Component cpt where cpt.dashboardId = :dashboardId")
    void deleteDashboardComponents(@Param("dashboardId") Long dashboardId);

    @Query("select cpt from Component cpt where cpt.dashboardId = :dashboardId order by cpt.type")
    List<Component> findAllDashboardComponents(@Param("dashboardId") Long dashboardId);
}
