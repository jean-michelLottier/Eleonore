package com.dashboard.eleonore.dashboard.exception;

import com.dashboard.eleonore.profile.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class to handle service response according to an exception thrown.
 */
@ControllerAdvice
public class DashboardExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(DashboardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String dashboardNotFoundHandler(DashboardNotFoundException dnfe) {
        return dnfe.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CustomerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String customerException(CustomerException ce) {
        return ce.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String authenticationException(AuthenticationException ae) {
        return ae.getMessage();
    }
}
