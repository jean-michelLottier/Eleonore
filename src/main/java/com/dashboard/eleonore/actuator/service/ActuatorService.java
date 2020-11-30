package com.dashboard.eleonore.actuator.service;

import com.dashboard.eleonore.actuator.ot.ActuatorOT;
import com.dashboard.eleonore.actuator.ot.EndPointOT;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ActuatorService {
    CompletableFuture<ActuatorOT> getActuators(Long profileId, Long actuatorId);

    CompletableFuture<List<EndPointOT>> getMetrics(Long profileId, Long actuatorId);

    CompletableFuture<EndPointOT> getMetric(Long profileId, Long actuatorId, String metricName, String arg);
}
