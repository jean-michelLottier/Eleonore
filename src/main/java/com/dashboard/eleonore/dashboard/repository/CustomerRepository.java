package com.dashboard.eleonore.dashboard.repository;

import com.dashboard.eleonore.dashboard.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("delete from Customer c where c.dashboardId = :dashboardId and c.profileId = :profileId")
    void delete(@Param("dashboardId") Long dashboardId, @Param("profileId") Long profileId);

    @Query("select c from Customer c where c.dashboardId = :dashboardId")
    List<Customer> findAllByDashboardId(@Param("dashboardId") Long dashboardId);
}
