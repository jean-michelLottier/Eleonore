package com.dashboard.eleonore.sonar.service;

import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.element.sonar.dto.SonarDTO;
import com.dashboard.eleonore.element.sonar.dto.SonarMetricDTO;
import com.dashboard.eleonore.sonar.ot.SonarOT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
public class SonarServiceImpl implements SonarService {
    private final ElementService<SonarDTO> elementService;

    public SonarServiceImpl(ElementService<SonarDTO> elementService) {
        this.elementService = elementService;
    }

    @Override
    public SonarOT getMeasuresComponent(Long profileId, Long sonarId) throws IOException, InterruptedException {
        if (profileId == null || sonarId == null) {
            return null;
        }

        StringBuilder url = new StringBuilder();
        this.elementService.getElement(profileId, sonarId)
                .ifPresent(elementDTO -> url.append(buildURL(elementDTO)));

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        String body = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        return new ObjectMapper().readValue(body, SonarOT.class);
    }

    @Override
    public CompletableFuture<SonarOT> getMeasuresComponentAsync(Long profileId, Long sonarId) {
        Optional<SonarDTO> optionalElementDTO = this.elementService.getElement(profileId, sonarId);
        StringBuilder url = new StringBuilder();
        optionalElementDTO.ifPresent(elementDTO -> url.append(buildURL(elementDTO)));

        if (StringUtils.isEmpty(url)) {
            return CompletableFuture.failedFuture(new NullPointerException("Empty url"));
        }

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
                        return mapper.readValue(body, SonarOT.class);
                    } catch (IOException ioe) {
                        throw new CompletionException(ioe);
                    }
                });
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
