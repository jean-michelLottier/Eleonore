package com.dashboard.eleonore.sonar.service;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.dto.SonarDTO;
import com.dashboard.eleonore.element.dto.SonarMetricDTO;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.http.HttpService;
import com.dashboard.eleonore.sonar.ot.SonarOT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
public class SonarServiceImpl extends HttpService<SonarOT> implements SonarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarServiceImpl.class);

    @Autowired
    private ElementService elementService;

    @Override
    public HttpResponse<SonarOT> getMeasuresComponent(Long profileId, Long sonarId) {
        if (profileId == null || sonarId == null) {
            return null;
        }

        Optional<ElementDTO> optionalElementDTO = this.elementService.getElement(profileId, sonarId, ElementType.SONAR);
        StringBuilder url = new StringBuilder();
        optionalElementDTO.ifPresent(elementDTO -> url.append(buildURL((SonarDTO) elementDTO)));

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        try {
            return this.get(request);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Fail to get sonar metric information", e);
            return null;
        }
    }

    @Override
    public CompletableFuture<ResponseEntity<SonarOT>> getMeasuresComponentAsync(Long profileId, Long sonarId,
                                                                                Function<HttpResponse<SonarOT>, ResponseEntity<SonarOT>> callback) {
        Optional<ElementDTO> optionalElementDTO = this.elementService.getElement(profileId, sonarId, ElementType.SONAR);
        StringBuilder url = new StringBuilder();
        optionalElementDTO.ifPresent(elementDTO -> url.append(buildURL((SonarDTO) elementDTO)));

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return this.getAsync(request, callback);
    }

    @Override
    public void POST(SonarOT body) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<SonarOT> getTClass() {
        return SonarOT.class;
    }

    private String buildURL(SonarDTO sonarDTO) {
        StringBuilder url = new StringBuilder();
        url.append(sonarDTO.getUrl());

        if (!sonarDTO.getUrl().endsWith("/")) {
            url.append("/");
        }

        if (!sonarDTO.getUrl().endsWith("api")) {
            url.append("api");
        }

        url.append("/measures/component?component=")
                .append(sonarDTO.getProjectKey())
                .append("&metricKeys=")
                .append(sonarDTO.getSonarMetrics()
                        .stream()
                        .map(SonarMetricDTO::getMetric)
                        .reduce("", (m1, m2) -> m1 + "," + m2));

        return url.toString();
    }
}
