package com.dashboard.eleonore.sonar;

import com.dashboard.eleonore.sonar.service.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sonar")
public class SonarController {

    @Autowired
    private SonarService sonarService;

    @GetMapping(value = "/metrics")
    public String getMetrics() {
        return this.sonarService.getMeasuresComponent();
    }
}
