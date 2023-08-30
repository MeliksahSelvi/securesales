package com.meliksah.securesales.exception;

import com.meliksah.securesales.enumeration.ErrorMessage;

/**
 * @Author mselvi
 * @Created 19.08.2023
 */

public class TokenException extends ApiException {
    public TokenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
