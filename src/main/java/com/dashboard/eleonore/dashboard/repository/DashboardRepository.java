package com.dashboard.eleonore.dashboard.repository;

import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    /**
     * Method to find a dashboard by its id.
     *
     * @param id
     * @param profileId
     * @return
     */
    @Query("select d from Dashboard d where d.id = (select c.dashboardId from Customer c where c.profileId = :profileId and c.dashboardId = :id)")
    Optional<Dashboard> findById(@Param("id") Long id, @Param("profileId") Long profileId);

    /**
     * Method to find a dashboard by its name.
     *
     * @param name
     * @return
     */
    @Query("select d from Dashboard d where d.name = :name and d.id in (select c.dashboardId from Customer c where c.profileId = :profileId)")
    Optional<Dashboard> findByName(@Param("name") String name, @Param("profileId") Long profileId);

    /**
     * Method to find the list of dashboards for a given profile.
     *
     * @param profileId
     * @return
     */
    @Query("select d from Dashboard d where d.id in (select c.dashboardId from Customer c where c.profileId = :profileId)")
    List<Dashboard> findAllByProfileId(@Param("profileId") Long profileId);

    @Modifying
    @Query("delete from Dashboard d where d.id = (select c.dashboardId from Customer c where c.profileId = :profileId and c.dashboardId = :id)")
    void deleteById(@Param("id") Long id, @Param("profileId") Long profileId);

    @Modifying
    @Query("delete from Dashboard d where d.name = :name and d.id in (select c.dashboardId from Customer c where c.profileId = :profileId)")
    void deleteByName(@Param("name") String name, @Param("profileId") Long profileId);
}
