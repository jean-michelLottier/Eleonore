package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.repository.entity.ElementType;

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

    /**
     * Method to save a dashboard element in database according to its type
     * @param dashboardId
     * @param elementDTO
     * @return
     */
    ElementDTO saveElement(Long dashboardId, ElementDTO elementDTO);

    /**
     * Method to delete a dashboard element
     * @param dashboardId
     * @param elementDTO
     */
    void deleteElement(Long dashboardId, ElementDTO elementDTO);

    /**
     * Method to delete a dashboard element (secure way)
     * @param profileId
     * @param dashboardId
     * @param elementId
     * @param elementType
     */
    void deleteElement(Long profileId, Long dashboardId, Long elementId, ElementType elementType);
}
