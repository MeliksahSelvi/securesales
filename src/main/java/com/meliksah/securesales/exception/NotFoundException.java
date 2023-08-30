package com.meliksah.securesales.exception;

import com.meliksah.securesales.enumeration.ErrorMessage;
/**
 * @Author mselvi
 * @Created 16.08.2023
 */

public class NotFoundException extends ApiException {
    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
