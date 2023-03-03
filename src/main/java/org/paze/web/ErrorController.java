package org.paze.web;

import org.paze.exception.CreatePaymentException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(CreatePaymentException.class)
    public String storageException(final CreatePaymentException exception,
                                   final Model model) {
        model.addAttribute("errorCode", exception.getErrorCode());
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("errorDescription", exception.getErrorMessage());
        return "error";
    }
}
