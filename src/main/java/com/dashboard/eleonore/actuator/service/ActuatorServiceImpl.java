package com.dashboard.eleonore.actuator.service;

import com.dashboard.eleonore.actuator.ot.*;
import com.dashboard.eleonore.element.actuator.dto.ActuatorDTO;
import com.dashboard.eleonore.element.service.ElementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ActuatorServiceImpl implements ActuatorService {
    private final ElementService<ActuatorDTO> elementService;

    @Autowired
    public ActuatorServiceImpl(ElementService<ActuatorDTO> elementService) {
        this.elementService = elementService;
    }

    @Override
    public CompletableFuture<ActuatorOT> getActuators(Long profileId, Long actuatorId) {

        Optional<ActuatorDTO> optionalActuatorDTO = elementService.getElement(profileId, actuatorId);
        StringBuilder url = new StringBuilder();
        optionalActuatorDTO.ifPresent(actuatorDTO -> url.append(buildURL(actuatorDTO, null)));

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return new ObjectMapper().readValue(body, ActuatorOT.class);
                    } catch (IOException ioe) {
                        throw new CompletionException(ioe);
                    }
                });
    }

    @Override
    public CompletableFuture<List<EndPointOT>> getMetrics(Long profileId, Long actuatorId) {
        Optional<ActuatorDTO> optionalActuatorDTO = elementService.getElement(profileId, actuatorId);

        HttpClient client = HttpClient.newHttpClient();

        List<HttpRequest> requests = new ArrayList<>();
        if (optionalActuatorDTO.isPresent()) {
            ActuatorDTO actuatorDTO = optionalActuatorDTO.get();
            // Prepare requests for all metrics
            requests = actuatorDTO.getActuatorMetrics().stream()
                    .map(metric -> URI.create(buildURL(actuatorDTO, metric.getMetric())))
                    .map(HttpRequest::newBuilder)
                    .map(reqBuilder -> reqBuilder.GET().build())
                    .collect(Collectors.toList());
        }

        // Send requests
        List<CompletableFuture<EndPointOT>> completableFutures = requests.parallelStream()
                .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(getObjectMapper(request, false)))
                .collect(Collectors.toList());

        // Group all future responses retrieved
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

        // Change the group return to get a list of EndPoint
        return allFutures.thenApply(future -> completableFutures.stream().map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<EndPointOT> getMetric(Long profileId, Long actuatorId, String metricName, String arg) {
        StringBuilder url = new StringBuilder();
        String prefixURL = metricName + (arg != null ? "/" + arg : "");
        elementService.getElement(profileId, actuatorId)
                .ifPresent(actuatorDTO -> url.append(buildURL(actuatorDTO, prefixURL)));

        var request = HttpRequest.newBuilder(URI.create(url.toString()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(getObjectMapper(request, !StringUtils.isEmpty(arg)));
    }

    private Function<String, EndPointOT> getObjectMapper(HttpRequest request, boolean hasArg) {
        String url = request.uri().getPath();
        final String metricName;
        if (hasArg) {
            if (url.contains("metrics")) {
                metricName = "metric";
            } else {
                throw new IllegalArgumentException("No corresponding metric found");
            }
        } else {
            if (url.endsWith("/")) {
                url = url.substring(0, url.lastIndexOf("/"));
            }
            metricName = url.substring(url.lastIndexOf("/") + 1);
        }

        return body -> {
            try {
                switch (metricName) {
                    case "auditevents":
                        return new ObjectMapper().readValue(body, AuditEventsOT.class);
                    case "info":
                        return new ObjectMapper().readValue(body, InfoOT.class);
                    case "loggers":
                        return new ObjectMapper().readValue(body, LoggersOT.class);
                    case "beans":
                        return new ObjectMapper().readValue(body, BeansOT.class);
                    case "caches":
                        return new ObjectMapper().readValue(body, CachesOT.class);
                    case "conditions":
                        return new ObjectMapper().readValue(body, ConditionsOT.class);
                    case "configprops":
                        return new ObjectMapper().readValue(body, ConfigPropsOT.class);
                    case "env":
                        return new ObjectMapper().readValue(body, EnvironmentOT.class);
                    case "health":
                        return new ObjectMapper().readValue(body, HealthOT.class);
                    case "httptrace":
                        return new ObjectMapper().readValue(body, TracesOT.class);
                    case "mappings":
                        return new ObjectMapper().readValue(body, MappingsOT.class);
                    case "metrics":
                        return new ObjectMapper().readValue(body, MetricsOT.class);
                    case "metric":
                        return new ObjectMapper().readValue(body, MetricOT.class);
                    case "scheduledtasks":
                        return new ObjectMapper().readValue(body, ScheduledTasksOT.class);
                    case "threaddump":
                        return new ObjectMapper().readValue(body, ThreadDumpOT.class);
                    default:
                        throw new IOException("No corresponding metric found");
                }
            } catch (IOException ioe) {
                throw new CompletionException(ioe);
            }
        };
    }

    private String buildURL(ActuatorDTO actuatorDTO, String prefixUrl) {
        StringBuilder url = new StringBuilder();
        url.append(actuatorDTO.getUrl());

        if (!actuatorDTO.getUrl().endsWith("/")) {
            url.append("/");
        }

        url.append("actuator/");

        if (!StringUtils.isEmpty(prefixUrl)) {
            url.append(prefixUrl);
        }

        return url.toString();
    }
}
