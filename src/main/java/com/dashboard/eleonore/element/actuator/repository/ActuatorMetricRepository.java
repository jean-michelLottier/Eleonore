package com.dashboard.eleonore.element.actuator.repository;

import com.dashboard.eleonore.element.actuator.repository.entity.ActuatorMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActuatorMetricRepository extends JpaRepository<ActuatorMetric, Long> {
    @Modifying
    @Query("delete from ActuatorMetric am where am.actuator.id = (\n" +
            "select c.elementId from Component c where c.elementId = :elementId " +
            "and c.type = com.dashboard.eleonore.element.repository.entity.ElementType.ACTUATOR and c.dashboardId = (\n" +
            "select cst.dashboardId from Customer cst where cst.profileId = :profileId and cst.dashboardId = :dashboardId and cst.editable=true))")
    void delete(@Param("profileId") Long profileId, @Param("dashboardId") Long dashboardId, @Param("elementId") Long elementId);

    @Query("select am from ActuatorMetric am where am.actuator.id = :actuatorId")
    List<ActuatorMetric> findById(@Param("actuatorId") long actuatorId);
}
