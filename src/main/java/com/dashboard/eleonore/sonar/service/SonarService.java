package com.dashboard.eleonore.sonar.service;


import com.dashboard.eleonore.sonar.ot.SonarOT;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface SonarService {
    /**
     * Synchronous method to get sonar metrics project.
     *
     * @param profileId
     * @param sonarId
     * @return
     */
    HttpResponse<SonarOT> getMeasuresComponent(Long profileId, Long sonarId);

    /**
     * Asynchronous method to get sonar metrics project.
     *
     * @param profileId
     * @param sonarId
     * @param callback
     * @return
     */
    CompletableFuture<ResponseEntity<SonarOT>> getMeasuresComponentAsync(Long profileId, Long sonarId, Function<HttpResponse<SonarOT>, ResponseEntity<SonarOT>> callback);
}
