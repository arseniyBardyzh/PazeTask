package org.paze.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paze.exception.CreatePaymentException;
import org.paze.model.Error;
import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import org.paze.model.UnauthorizedError;
import org.paze.repository.api.PazeWebRepository;
import org.paze.repository.impl.PazeWebRepositoryImpl;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PazeRepositoryTest {
    public static MockWebServer webServer;

    PazeWebRepository pazeWebRepository;

    public ObjectMapper objectMapper = new ObjectMapper();
    WebClient webClient = WebClient.create();

    @BeforeAll
    static void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        webClient = WebClient.builder().baseUrl(webServer.url("/").url().toString()).build();
        pazeWebRepository = new PazeWebRepositoryImpl(webClient);
    }

    @Test
    void createPayment_shouldReturnCorrectResponse_WhenDataIsCorrectAndServerAvailable() throws Exception {
        PaymentRequest paymentRequest = objectMapper.readValue(PaymentRequest.class.getResourceAsStream("/json/GoodRequest.json"), PaymentRequest.class);
        PaymentResponse paymentResponse = objectMapper.readValue(PaymentResponse.class.getResourceAsStream("/json/GoodResponse.json"), PaymentResponse.class);
        webServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(objectMapper.writeValueAsString(paymentResponse))
                .addHeader("Content-Type", "application/json"));
        PaymentResponse response = pazeWebRepository.createPayment(paymentRequest).block();
        assertEquals(paymentResponse.getResult().getRedirectUrl(), response.getResult().getRedirectUrl());
        RecordedRequest recordedRequest = webServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/payments", recordedRequest.getPath());
    }

    @Test
    void createPayment_shouldReturnBadRequestDto_WhenSomethingWentWrong() throws Exception {
        PaymentRequest paymentRequest = objectMapper.readValue(PaymentRequest.class.getResourceAsStream("/json/GoodRequest.json"), PaymentRequest.class);
        Error error = objectMapper.readValue(Error.class.getResourceAsStream("/json/BadRequest.json"), Error.class);
        webServer.enqueue(new MockResponse().setResponseCode(400)
                .setBody(objectMapper.writeValueAsString(error))
                .addHeader("Content-Type", "application/json"));
        CreatePaymentException response = assertThrows(CreatePaymentException.class, () ->
                                                pazeWebRepository.createPayment(paymentRequest).block());
        assertEquals(400, response.getErrorCode());
        assertEquals(error.getError(), response.getMessage());
        RecordedRequest recordedRequest = webServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/payments", recordedRequest.getPath());
    }

    @Test
    void createPayment_shouldReturnUnauthorizedException_WhenAuthorizationFailed() throws Exception {
        PaymentRequest paymentRequest = objectMapper.readValue(PaymentRequest.class.getResourceAsStream("/json/GoodRequest.json"), PaymentRequest.class);
        UnauthorizedError error = objectMapper.readValue(UnauthorizedError.class.getResourceAsStream("/json/Unauthorized.json"), UnauthorizedError.class);
        webServer.enqueue(new MockResponse().setResponseCode(401)
                .setBody(objectMapper.writeValueAsString(error))
                .addHeader("Content-Type", "application/json"));
        CreatePaymentException response = assertThrows(CreatePaymentException.class, () ->
                                                pazeWebRepository.createPayment(paymentRequest).block());
        assertEquals(401, response.getErrorCode());
        assertEquals(error.getMessage(), response.getErrorMessage());
        RecordedRequest recordedRequest = webServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/payments", recordedRequest.getPath());
    }
}
