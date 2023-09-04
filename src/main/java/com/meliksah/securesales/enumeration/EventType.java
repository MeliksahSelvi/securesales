package com.meliksah.securesales.enumeration;

/**
 * @Author mselvi
 * @Created 08.08.2023
 */

public enum EventType {

    LOGIN_ATTEMPT("Login Attempt"),
    LOGIN_ATTEMPT_FAILURE("Login Attempt Failure"),
    LOGIN_ATTEMPT_SUCCESS("Login Attempt Success"),
    PROFILE_UPDATE("Profile Update"),
    PROFILE_PICTURE_UPDATE("Profile Picture Update"),
    ROLE_UPDATE("Role Update"),
    ACCOUNT_SETTINGS_UPDATE("Account Settings Update"),
    PASSWORD_UPDATE("Password Update"),
    MFA_UPDATE("MFA Update");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
