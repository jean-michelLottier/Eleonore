package com.dashboard.eleonore.sonar.service;

import com.dashboard.eleonore.http.HttpService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
public class SonarServiceImpl extends HttpService<SonarOT> implements SonarService {
    @Override
    public HttpResponse<SonarOT> getMeasuresComponent() {
        var request = HttpRequest.newBuilder(URI.create(url + "/measures/component?metricKeys=complexity,coverage,security_rating&componentKey=sonar-dashboard-key"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        try {
            return this.get(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<ResponseEntity<SonarOT>> getMeasuresComponentAsync(Function<HttpResponse<SonarOT>, ResponseEntity<SonarOT>> callback) {
        var request = HttpRequest.newBuilder(URI.create(url + "/measures/component?metricKeys=complexity,coverage,security_rating&componentKey=sonar-dashboard-key"))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        return this.getAsync(request, callback);
    }

    @Override
    public void POST(SonarOT body) {

    }

    @Override
    public Class<SonarOT> getTClass() {
        return SonarOT.class;
    }
}
