package org.paze.repository.impl;

import org.paze.exception.CreatePaymentException;
import org.paze.model.Error;
import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import org.paze.repository.api.PazeWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class PazeWebRepositoryImpl implements PazeWebRepository {

    private final WebClient webClient;

    @Autowired
    public PazeWebRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<PaymentResponse> createPayment(PaymentRequest requestDto) {
        return webClient.post()
                        .uri("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(requestDto), PaymentRequest.class)
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse ->
                                clientResponse.bodyToMono(Error.class)
                                        .handle((error, sink) ->
                                                sink.error(new CreatePaymentException(error.getError(),
                                                        error.getStatus().intValue(),
                                                        error.getMessage()))
                                        )
                        )
                        .bodyToMono(PaymentResponse.class);
    }
}
