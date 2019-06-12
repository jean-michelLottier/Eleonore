package com.dashboard.eleonore.sonar;

import com.dashboard.eleonore.sonar.service.SonarOT;
import com.dashboard.eleonore.sonar.service.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sonar")
public class SonarController {

    @Autowired
    private SonarService sonarService;

    @GetMapping(value = "/metrics")
    public ResponseEntity<SonarOT> getMetrics() {
        HttpResponse<SonarOT> response = this.sonarService.getMeasuresComponent();
        if (response.statusCode() != 200) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.body(), HttpStatus.OK);
    }

    @GetMapping(value = "/metrics/async")
    @Async
    public CompletableFuture<ResponseEntity<SonarOT>> getMetricsAsync() {
        return this.sonarService.getMeasuresComponentAsync(response -> ResponseEntity.ok(response.body()));
    }
}
