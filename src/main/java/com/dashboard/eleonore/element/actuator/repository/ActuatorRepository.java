package com.dashboard.eleonore.element.actuator.repository;

import com.dashboard.eleonore.element.actuator.repository.entity.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Long> {
    @Query("select a from Actuator a where a.id in (\n" +
            "select cpt.elementId from Component cpt where cpt.dashboardId = :dashboardId " +
            "and cpt.type = com.dashboard.eleonore.element.repository.entity.ElementType.ACTUATOR)")
    List<Actuator> findAllByDashboardId(@Param("dashboardId") Long dashboardId);

    @Modifying
    @Query("delete from Actuator a where a.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId " +
            "and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.ACTUATOR and c.dashboardId = (\n" +
            "select cst.dashboardId from Customer cst where cst.profileId = :profileId and cst.dashboardId = :dashboardId and cst.editable=true))")
    void delete(@Param("profileId") Long profileId, @Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId);

    @Query("select a from Actuator a where a.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId " +
            "and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.ACTUATOR and c.dashboardId in (\n" +
            "select cst.dashboardId from Customer cst where cst.profileId = :profileId))")
    Optional<Actuator> find(@Param("profileId") Long profileId, @Param("elementId") Long elementId);
}
