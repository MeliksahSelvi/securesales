package com.meliksah.securesales.enumeration;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

public enum VerificationType {

    ACCOUNT("account"),
    PASSWORD("password");

    private final String type;

    VerificationType(String type) {
        this.type = type;
    }

    public String toString(){
        return type.toLowerCase();
    }
}
