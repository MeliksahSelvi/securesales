package com.meliksah.securesales.exception;

import com.meliksah.securesales.enumeration.ErrorMessage;

/**
 * @Author mselvi
 * @Created 16.08.2023
 */

public class UrlException extends ApiException {
    public UrlException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
