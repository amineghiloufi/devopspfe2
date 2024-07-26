package com.example.demoo.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.exceptions.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;


@RestControllerAdvice
public class ExceptionHandling implements ErrorController{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact the administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed in this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERR_MSG = "Sorry.An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username or password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error please contact the administration";
    private static final String ERR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "Access denied. You do not have enough permission";
    public static final String ERROR_PATH = "/error";
    public static final String CATEGORY_TRY_AGAIN = "Category Not Found. Please try again.";
    public static final String BRAND_TRY_AGAIN = "Brand Not Found. Please try again.";
    public static final String MODEL_TRY_AGAIN = "Model Not Found. Please try again.";

    public static final String VEHICLE_TRY_AGAIN = "Vehicle Not Found. Please try again.";


    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase()), httpStatus);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERR_MSG);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERR_PROCESSING_FILE);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(HttpStatus.NOT_FOUND, "Page Not Found");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<HttpResponse> categoryNotFoundException(CategoryNotFoundException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, CATEGORY_TRY_AGAIN);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<HttpResponse> brandNotFoundException(BrandNotFoundException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, BRAND_TRY_AGAIN);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<HttpResponse> modelNotFoundException(ModelNotFoundException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, MODEL_TRY_AGAIN);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<HttpResponse> vehicleNotFoundException(VehicleNotFoundException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, VEHICLE_TRY_AGAIN);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
