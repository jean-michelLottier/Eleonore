package com.dashboard.eleonore.sonar;

import com.dashboard.eleonore.BaseController;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.service.ProfileService;
import com.dashboard.eleonore.sonar.ot.SonarOT;
import com.dashboard.eleonore.sonar.service.SonarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/sonar")
public class SonarController extends BaseController {

    private final SonarService sonarService;

    @Autowired
    public SonarController(ProfileService profileService, SonarService sonarService) {
        super(profileService);
        this.sonarService = sonarService;
    }

    @GetMapping(value = "/metrics")
    public ResponseEntity<SonarOT> getMetrics(HttpServletRequest request, @RequestParam(name = "id") String sonarIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        long sonarId;
        try {
            sonarId = Long.parseLong(sonarIdStr);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            SonarOT sonarOT = this.sonarService.getMeasuresComponent(profileDTO.getAuthentication().getProfileId(), sonarId);
            return ResponseEntity.ok(sonarOT);
        } catch (IOException ioe) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/metrics/async")
    @Async
    public CompletableFuture<ResponseEntity<SonarOT>> getMetricsAsync(HttpServletRequest request, @RequestParam(name = "id") String sonarIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        long sonarId;
        try {
            sonarId = Long.parseLong(sonarIdStr);
        } catch (NumberFormatException nfe) {
            return CompletableFuture.failedFuture(nfe);
        }

        return this.sonarService.getMeasuresComponentAsync(profileDTO.getAuthentication().getProfileId(),
                sonarId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error("An error occurred with the request sent", throwable);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
