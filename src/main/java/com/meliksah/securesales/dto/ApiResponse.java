package com.meliksah.securesales.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Author mselvi
 * @Created 30.08.2023
 */

@Getter
@Setter
public class ApiResponse implements Serializable {

    private HttpStatus status;
    private int statusCode;
    private Map<?, ?> data;
    private String message;
    private String reason;
    private Date timeStamp;

    private ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.timeStamp = new Date();
    }

    private ApiResponse(HttpStatus status, Map<?, ?> data, String message) {
        this(status, message);
        this.data = data;
    }

    private ApiResponse(HttpStatus status, String message, String reason) {
        this(status, message);
        this.reason = reason;
    }

    private ApiResponse(HttpStatus status, Map<?, ?> data, String message, String reason) {
        this(status, data, message);
        this.reason = reason;
    }

    public static ApiResponse of(HttpStatus httpStatus, String message) {
        return new ApiResponse(httpStatus, message);
    }

    public static ApiResponse of(HttpStatus httpStatus, Map<?, ?> data, String message) {
        return new ApiResponse(httpStatus, data, message);
    }

    public static ApiResponse of(HttpStatus httpStatus, String message, String reason) {
        return new ApiResponse(httpStatus, message, reason);
    }

    public static ApiResponse of(HttpStatus httpStatus, Map<?, ?> data, String message, String reason) {
        return new ApiResponse(httpStatus, data, message, reason);
    }

}
