package com.meliksah.securesales.exception;

import com.meliksah.securesales.enumeration.ErrorMessage;
/**
 * @Author mselvi
 * @Created 18.08.2023
 */

public class PasswordException extends ApiException {
    public PasswordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
