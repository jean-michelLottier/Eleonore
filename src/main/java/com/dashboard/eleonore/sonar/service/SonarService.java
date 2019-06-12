package com.dashboard.eleonore.sonar.service;


import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface SonarService {
    String url = "http://192.168.99.100:9000/api";

    HttpResponse<SonarOT> getMeasuresComponent();

    CompletableFuture<ResponseEntity<SonarOT>> getMeasuresComponentAsync(Function<HttpResponse<SonarOT>, ResponseEntity<SonarOT>> callback);
}
