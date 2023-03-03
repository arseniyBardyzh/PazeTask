package org.paze.utils;

import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import org.paze.model.PaymentType;
import org.paze.model.Result;

import java.math.BigDecimal;

public class PazeTestDataUtils {

    public static PaymentRequest createPaymentRequest(){
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.ONE);
        paymentRequest.setPaymentType(PaymentType.DEPOSIT);
        paymentRequest.setCurrency("EUR");
        return paymentRequest;
    }

    public static PaymentResponse createPaymentResponse(){
        PaymentResponse paymentResponse = new PaymentResponse();
        Result result = new Result();
        result.setRedirectUrl("testUrl");
        paymentResponse.setResult(result);
        return paymentResponse;
    }
}
