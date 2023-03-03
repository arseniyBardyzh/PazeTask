package org.paze.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paze.exception.CreatePaymentException;
import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import org.paze.repository.api.PazeWebRepository;
import org.paze.service.api.PazeService;
import org.paze.service.impl.PazeServiceImpl;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.paze.utils.PazeTestDataUtils.createPaymentRequest;
import static org.paze.utils.PazeTestDataUtils.createPaymentResponse;

@ExtendWith(MockitoExtension.class)
class PazeServiceTest {
    @Mock
    private PazeWebRepository pazeWebRepository;

    @InjectMocks
    PazeService pazeService = new PazeServiceImpl(pazeWebRepository);

    @Test
    void createPayment_shouldReturnGoodResponse_whenDataIsCorrect(){

        PaymentRequest paymentRequest = createPaymentRequest();
        PaymentResponse paymentResponse = createPaymentResponse();
        when(pazeWebRepository.createPayment(paymentRequest)).thenReturn(Mono.just(paymentResponse));

        String url = pazeService.getRedirectUrl(BigDecimal.ONE);

        assertEquals("testUrl", url);
    }

    @Test
    void createPayment_shouldThrowException_whenResponseObjectIsNull(){

        PaymentRequest paymentRequest = createPaymentRequest();
        when(pazeWebRepository.createPayment(paymentRequest)).thenReturn(null);

        assertThrows(CreatePaymentException.class, () -> pazeService.getRedirectUrl(BigDecimal.ONE));
    }

    @Test
    void createPayment_shouldThrowException_whenResponseResultIsNull(){

        PaymentRequest paymentRequest = createPaymentRequest();
        PaymentResponse paymentResponse = createPaymentResponse();
        paymentResponse.setResult(null);
        when(pazeWebRepository.createPayment(paymentRequest)).thenReturn(Mono.just(paymentResponse));

        assertThrows(CreatePaymentException.class, () -> pazeService.getRedirectUrl(BigDecimal.ONE));
    }

    @Test
    void createPayment_shouldThrowException_whenRedirectUrlIsNull(){

        PaymentRequest paymentRequest = createPaymentRequest();
        PaymentResponse paymentResponse = createPaymentResponse();
        paymentResponse.getResult().setRedirectUrl(null);
        when(pazeWebRepository.createPayment(paymentRequest)).thenReturn(Mono.just(paymentResponse));

        CreatePaymentException exception = assertThrows(CreatePaymentException.class, () -> pazeService.getRedirectUrl(BigDecimal.ONE));
        assertEquals("Redirect Url didn't find in response entity", exception.getMessage());
    }

    @Test
    void createPayment_shouldThrowException_whenExtensionServiceReturnBadRequest(){

        PaymentRequest paymentRequest = createPaymentRequest();
        when(pazeWebRepository.createPayment(paymentRequest)).thenThrow(CreatePaymentException.class);
        assertThrows(CreatePaymentException.class, () -> pazeService.getRedirectUrl(BigDecimal.ONE));

    }
}
