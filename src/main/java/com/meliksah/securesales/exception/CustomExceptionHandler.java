package com.meliksah.securesales.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.meliksah.securesales.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 17.08.2023
 */

@RestControllerAdvice
@RestController
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {


    @ExceptionHandler(QueryException.class)
    public final ApiResponse handleQueryException(QueryException exception, WebRequest webRequest){
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Hibernate Query Language Is Wrong";

        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null, message, reason);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public final ApiResponse handleTokenExpiredException(TokenExpiredException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Some Error Occured When Token is Validating";

        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null, message, reason);
    }

    @ExceptionHandler(TokenException.class)
    public final ApiResponse handleTokenException(TokenException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Some Error Occured When Token is Validating";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, null, message, reason);
    }


    @ExceptionHandler(PasswordException.class)
    public final ApiResponse handlePasswordException(PasswordException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Passwords Is Not Same";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, null, message, reason);
    }

    @ExceptionHandler(CodeException.class)
    public final ApiResponse handleCodeException(CodeException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Please Try Again With New Two Factor Verification Code";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(UrlException.class)
    public final ApiResponse handleUrlException(UrlException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Please Try Again Again";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ApiResponse handleNotFoundException(NotFoundException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Reason to fail: " + exception.getMessage();

        return ApiResponse.of(HttpStatus.NOT_FOUND, message, reason);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage());
        String reason = createReasonMessage(ex);
        String message = ex.getMessage();

        ApiResponse RestRapiResponsesponse = ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
        return new ResponseEntity<>(RestRapiResponsesponse, status);
    }

    private String createReasonMessage(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public final ApiResponse handleUserNameNotFoundException(UsernameNotFoundException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "User Not Found By Email";

        return ApiResponse.of(HttpStatus.NOT_FOUND, message, reason);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public final ApiResponse handleJWTVerificationException(JWTVerificationException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Jwt Token Cannot Be Verify";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ApiResponse handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = exception.getMessage().contains("Duplicate Entry") ? "Information Already Exists" : exception.getMessage();

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ApiResponse handleBadCredentialsException(BadCredentialsException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Incorrect Email Or Password";

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ApiResponse handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "Access Denied. You Don't Have Access";

        return ApiResponse.of(HttpStatus.FORBIDDEN, message, reason);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ApiResponse handleEmptyResultDataAccessException(EmptyResultDataAccessException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = exception.getMessage().contains("expected 1,actual 0") ? "Record Not Found" : exception.getMessage();

        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, reason);
    }

    @ExceptionHandler(LockedException.class)
    public final ApiResponse handleLockedException(LockedException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "This User is Locked.  Please contact to administrator";

        return ApiResponse.of(HttpStatus.LOCKED, message, reason);
    }

    @ExceptionHandler(DisabledException.class)
    public final ApiResponse handleDisabledException(DisabledException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = "This User Is Disabled.";

        return ApiResponse.of(HttpStatus.NOT_ACCEPTABLE, message, reason);
    }

    @ExceptionHandler(ApiException.class)
    public final ApiResponse handleApiException(ApiException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = webRequest.getDescription(false);

        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, message, reason);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ApiResponse handleRuntimeException(RuntimeException exception, WebRequest webRequest) {
        log.error(exception.getMessage());
        String message = exception.getMessage();
        String reason = webRequest.getDescription(false);

        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, message, reason);
    }
}
