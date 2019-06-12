package com.dashboard.eleonore.http;

import com.dashboard.eleonore.sonar.service.SonarOT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class HttpService<T> {

    public final HttpResponse<T> get(HttpRequest request) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        return client.send(request, new JsonBodyHandler<>(this.getTClass()));
    }

    @Async
    public CompletableFuture<ResponseEntity<T>> getAsync(HttpRequest request, Function<HttpResponse<T>, ResponseEntity<T>> callback) {
        var client = HttpClient.newHttpClient();
        return client.sendAsync(request, new JsonBodyHandler<>(this.getTClass()))
                .thenApply(callback)
                .exceptionally(throwable -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public abstract void POST(final T body);

    public abstract Class<T> getTClass();

    private class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
        private Class<T> type;

        JsonBodyHandler(Class<T> type) {
            this.type = type;
        }

        @Override
        public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
            return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofByteArray(),
                    bytes -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
                            return mapper.readValue(bytes, type);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }
}
