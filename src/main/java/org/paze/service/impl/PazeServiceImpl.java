package org.paze.service.impl;

import org.paze.exception.CreatePaymentException;
import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import org.paze.model.PaymentType;
import org.paze.repository.api.PazeWebRepository;
import org.paze.service.api.PazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PazeServiceImpl implements PazeService {

    private PazeWebRepository pazeWebRepository;

    @Autowired
    public PazeServiceImpl(final PazeWebRepository pazeWebRepository) {
        this.pazeWebRepository = pazeWebRepository;
    }

    @Override
    public String getRedirectUrl(BigDecimal amount) {
        PaymentRequest requestDto = createRequestDto(amount);
        try{
            PaymentResponse responseDto = pazeWebRepository.createPayment(requestDto).block();
            String redirectUrl = responseDto.getResult().getRedirectUrl();
            if(redirectUrl == null){
                throw new RuntimeException("Redirect Url didn't find in response entity");
            }
            return responseDto.getResult().getRedirectUrl();
        } catch (Exception e){
            if (e instanceof CreatePaymentException){
                throw e;
            }
            throw new CreatePaymentException(e.getMessage(), e.getCause());
        }
    }

    private PaymentRequest createRequestDto(BigDecimal amount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setCurrency("EUR");
        paymentRequest.setPaymentType(PaymentType.DEPOSIT);
        return paymentRequest;
    }
}
