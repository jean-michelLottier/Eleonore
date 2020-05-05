package com.dashboard.eleonore.sonar;

import com.dashboard.eleonore.http.BaseController;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.service.ProfileService;
import com.dashboard.eleonore.sonar.ot.SonarOT;
import com.dashboard.eleonore.sonar.service.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sonar")
public class SonarController extends BaseController {

    @Autowired
    private SonarService sonarService;

    @Autowired
    public SonarController(ProfileService profileService) {
        super(profileService);
    }

    @GetMapping(value = "/metrics")
    public ResponseEntity<SonarOT> getMetrics(HttpServletRequest request, @RequestParam(name = "id", required = true) String sonarIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        Long sonarId;
        try {
            sonarId = Long.valueOf(sonarIdStr);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        HttpResponse<SonarOT> response = this.sonarService.getMeasuresComponent(profileDTO.getAuthentication().getProfileId(), sonarId);
        if (response == null || response.statusCode() != 200) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.body(), HttpStatus.OK);
    }

    @GetMapping(value = "/metrics/async")
    @Async
    public CompletableFuture<ResponseEntity<SonarOT>> getMetricsAsync(HttpServletRequest request, @RequestParam(name = "id", required = true) String sonarIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        Long sonarId;
        try {
            sonarId = Long.valueOf(sonarIdStr);
        } catch (NumberFormatException nfe) {
            return CompletableFuture.failedFuture(nfe);
        }

        return this.sonarService.getMeasuresComponentAsync(profileDTO.getAuthentication().getProfileId(),
                sonarId, response -> ResponseEntity.ok(response.body()));
    }
}
