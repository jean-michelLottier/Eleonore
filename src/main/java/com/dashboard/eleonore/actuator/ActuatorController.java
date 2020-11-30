package com.dashboard.eleonore.actuator;

import com.dashboard.eleonore.BaseController;
import com.dashboard.eleonore.actuator.ot.ActuatorOT;
import com.dashboard.eleonore.actuator.ot.EndPointOT;
import com.dashboard.eleonore.actuator.service.ActuatorService;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
@RequestMapping("/api/actuator")
@Slf4j
public class ActuatorController extends BaseController {
    private final ActuatorService service;

    private static final String LOG_REQUEST_ERROR = "An error occurred with the request sent";

    @Autowired
    public ActuatorController(ProfileService profileService, ActuatorService actuatorService) {
        super(profileService);
        this.service = actuatorService;
    }

    @GetMapping
    @Async
    public CompletableFuture<ResponseEntity<Set<String>>> getActuators(HttpServletRequest request, @RequestParam(value = "id") Long actuatorId) {
        ProfileDTO profileDTO = this.checkSessionActive(request.getSession());

        Function<ActuatorOT, ResponseEntity<Set<String>>> callback = data -> {
            Set<String> actuators = data.getLinks().keySet();
            actuators.removeAll(Arrays.asList("integrationgraph", "self", "shutdown"));
            actuators.removeIf(el -> el.contains("-"));
            return ResponseEntity.ok(actuators);
        };

        return this.service.getActuators(profileDTO.getAuthentication().getProfileId(), actuatorId)
                .thenApply(callback)
                .exceptionally(throwable -> {
                    log.error(LOG_REQUEST_ERROR, throwable);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }


    @GetMapping(value = "/metrics")
    @Async
    public CompletableFuture<ResponseEntity<List<EndPointOT>>> getMetrics(HttpServletRequest request, @RequestParam(value = "id") Long actuatorId) {
        ProfileDTO profileDTO = this.checkSessionActive(request.getSession());

        return this.service.getMetrics(profileDTO.getAuthentication().getProfileId(), actuatorId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error(LOG_REQUEST_ERROR, throwable);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @GetMapping(value = "/metrics/{metric_name}")
    @Async
    public CompletableFuture<ResponseEntity<EndPointOT>> getMetric(HttpServletRequest request,
                                                                   @RequestParam(value = "id") Long actuatorId,
                                                                   @RequestParam(value = "arg", required = false) String arg,
                                                                   @PathVariable(value = "metric_name") String metricName) {
        ProfileDTO profileDTO = this.checkSessionActive(request.getSession());

        return this.service.getMetric(profileDTO.getAuthentication().getProfileId(), actuatorId, metricName, arg)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error(LOG_REQUEST_ERROR, throwable);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
