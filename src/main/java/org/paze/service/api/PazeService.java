package org.paze.service.api;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service request and analyse data from paze API
 */
public interface PazeService {
    /**
     * Get redirect url from paze API
     * @param amount amount from web UI
     * @return Url
     */
    String getRedirectUrl(BigDecimal amount);
}
