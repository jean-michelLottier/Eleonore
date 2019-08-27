package com.dashboard.eleonore.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    /**
     * Method to find a dashboard by its name.
     * @param name
     * @return
     */
    @Query("select d from Dashboard d where d.name = :name")
    Optional<Dashboard> findByName(@Param("name") String name);
}
