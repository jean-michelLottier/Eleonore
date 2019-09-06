package com.dashboard.eleonore.dashboard.service;

import com.dashboard.eleonore.dashboard.dto.CustomerDTO;
import com.dashboard.eleonore.dashboard.dto.DashboardDTO;

import java.util.List;
import java.util.Optional;

public interface DashboardService {
    /**
     * Method to save a new dashboard in database.
     *
     * @param dashboardDTO
     * @return
     */
    DashboardDTO saveDashboard(DashboardDTO dashboardDTO);

    /**
     * Method to delete a dashboard in database.
     *
     * @param id        dashboard id
     * @param profileId
     */
    void deleteDashboard(Long id, Long profileId);

    /**
     * Method to delete a dashboard in database.
     *
     * @param name      dashboard name
     * @param profileId
     */
    void deleteDashboard(String name, Long profileId);

    /**
     * Method to get dashboard by its id.
     *
     * @param id
     * @param profileId
     * @return
     */
    Optional<DashboardDTO> getDashboardById(Long id, Long profileId);

    /**
     * Method to get dashboard by its name.
     *
     * @param name
     * @param profileId
     * @return
     */
    Optional<DashboardDTO> getDashboardByName(String name, Long profileId);

    /**
     * Method to get the dashboard list for a given profile.
     *
     * @param profileId
     * @return
     */
    List<DashboardDTO> getDashboards(Long profileId);

    /**
     * Method to link a dashboard with a profile.
     *
     * @param customerDTO
     * @return
     */
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
}
