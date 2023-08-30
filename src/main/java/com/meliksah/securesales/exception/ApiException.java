package com.meliksah.securesales.exception;


import com.meliksah.securesales.enumeration.ErrorMessage;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

public class ApiException extends RuntimeException {

    public ApiException(ErrorMessage errorMessage){
        super(errorMessage.toString());
    }
}
