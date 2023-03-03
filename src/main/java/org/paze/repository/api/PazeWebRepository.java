package org.paze.repository.api;

import org.paze.model.PaymentRequest;
import org.paze.model.PaymentResponse;
import reactor.core.publisher.Mono;

/**
 * Repository to send requests to paze
 */
public interface PazeWebRepository {
    /**
     * Create Deposit Payment
     * @param paymentRequest dto with currency and amount
     * @return information about operation
     */
    Mono<PaymentResponse> createPayment(PaymentRequest paymentRequest);
}
