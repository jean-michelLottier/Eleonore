package com.dashboard.eleonore.component.service;

import com.dashboard.eleonore.component.dto.ElementDTO;

import java.util.List;

public interface ElementService {
    /**
     * Method to delete all elements belong to a given dashboard.
     *
     * @param dashboardId
     */
    void deleteElements(Long dashboardId);

    /**
     * Method to copy all elements belong to an original dashboard to a copy.
     *
     * @param dashboardIdOriginal
     * @param dashboardIdCopy
     */
    void copyDashboardElements(Long dashboardIdOriginal, Long dashboardIdCopy);

    /**
     * Method to get elements belong to a given dashboard.
     *
     * @param dashboardId
     * @return
     */
    List<ElementDTO> getElements(Long dashboardId);
}
