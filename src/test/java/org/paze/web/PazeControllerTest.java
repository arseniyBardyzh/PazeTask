package org.paze.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paze.service.api.PazeService;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PazeControllerTest {

    @Mock
    PazeService pazeService;

    @InjectMocks
    ApplicationController applicationController;

    @Test
    void sendNumber_ShouldReturnUrl_WhenDataIsCorrect(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(pazeService.getRedirectUrl(BigDecimal.ONE)).thenReturn("testLink");
        String redirectUrl = applicationController.sendNumber(BigDecimal.ONE);

        assertEquals("redirect:testLink", redirectUrl);
    }
}
