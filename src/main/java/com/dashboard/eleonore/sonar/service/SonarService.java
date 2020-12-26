package com.dashboard.eleonore.sonar.service;


import com.dashboard.eleonore.sonar.ot.SonarOT;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface SonarService {
    /**
     * Synchronous method to get sonar metrics project.
     *
     * @param profileId
     * @param sonarId
     * @return
     */
    SonarOT getMeasuresComponent(Long profileId, Long sonarId) throws IOException, InterruptedException;

    /**
     * Asynchronous method to get sonar metrics project.
     *
     * @param profileId
     * @param sonarId
     * @return
     */
    CompletableFuture<SonarOT> getMeasuresComponentAsync(Long profileId, Long sonarId);
}
