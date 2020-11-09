package com.dashboard.eleonore.element.service;

import com.dashboard.eleonore.element.dto.ElementDTO;

import java.util.Optional;

public interface ElementService<T extends ElementDTO> extends ElementsService {
    /**
     * Method to get element.
     *
     * @param profileId
     * @param elementId
     * @return
     */
    Optional<T> getElement(Long profileId, Long elementId);

    /**
     * Method to save a dashboard element in database according to its type
     *
     * @param dashboardId
     * @param elementDTO
     * @return
     */
    Optional<T> saveElement(Long dashboardId, T elementDTO);

    /**
     * Method to delete a dashboard element
     *
     * @param dashboardId
     * @param elementDTO
     */
    void deleteElement(Long dashboardId, T elementDTO);

    /**
     * Method to delete a dashboard element (secure way)
     *
     * @param profileId
     * @param dashboardId
     * @param elementId
     */
    void deleteElement(Long profileId, Long dashboardId, Long elementId);

    /**
     * Method to update a dashboard element in database according to its type
     *
     * @param elementDTO
     * @return
     */
    Optional<T> updateElement(T elementDTO);
}
