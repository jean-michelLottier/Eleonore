package com.dashboard.eleonore.sonar.service;

import org.springframework.stereotype.Component;

@Component
public class SonarServiceImpl implements SonarService {
    @Override
    public String getMeasuresComponent() {
        return "Metrics Sonar";
    }
}
