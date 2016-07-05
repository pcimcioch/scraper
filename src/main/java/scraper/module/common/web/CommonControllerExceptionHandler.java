package scraper.module.common.web;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import scraper.exception.ResourceNotFoundException;
import scraper.exception.UnexpectedException;
import scraper.exception.ValidationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handler for exceptions.
 * <p>
 * This handler will handle some of the exceptions that may be thrown to "http request" layer.
 */
@ControllerAdvice
public class CommonControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(HttpServletResponse res, ResourceNotFoundException ex) throws IOException {
        res.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(HttpServletResponse res, ValidationException ex) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletResponse res, IllegalArgumentException ex) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException(HttpServletResponse res, IllegalStateException ex) throws IOException {
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public void handleDataAccessException(HttpServletResponse res, DataAccessException ex) throws IOException {
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(UnexpectedException.class)
    public void handleUnexpectedException(HttpServletResponse res, UnexpectedException ex) throws IOException {
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
