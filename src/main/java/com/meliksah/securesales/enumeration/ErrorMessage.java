package com.meliksah.securesales.enumeration;

/**
 * @Author mselvi
 * @Created 30.08.2023
 */

public enum ErrorMessage {

    NO_ROLE_FOUND_BY_NAME("No Role Found By Name"),
    NO_USER_FOUND_BY_ID("No User Found By Id"),
    NO_USER_FOUND_BY_EMAIL("No User Found By Email"),
    NO_USER_FOUND_BY_CODE("No User Found By Two Factor Code"),
    NO_USER_FOUND_BY_URL("No User Found By Reset Password Url"),
    NO_ROLE_FOUND_BY_USER_ID("No Role Found By User Id"),
    NO_TWO_FACTOR_VERIFICATION_FOUND_BY_CODE("No Found Two Factor Verification By Code"),
    NO_PASSWORD_VERIFICATION_FOUND_BY_URL("No Password Verification By Url"),
    NO_ACCOUNT_VERIFICATION_FOUND_BY_URL("No Account Verification By Url"),
    EMAIL_ALREADY_IN_USE("Email Already In Use"),
    CODE_IS_INVALID("Code Is Invalid"),
    CODE_IS_EXPIRED("Code Is Expired"),
    URL_IS_INVALID("Url Is Invalid"),
    URL_IS_EXPIRED("Url Is Expired"),
    URL_IS_ALREADY_VERIFIED("User Is Already Verified"),
    PASSWORDS_IS_NOT_SAME("Passwords Are Not Same"),
    WRONG_CURRENT_PASSWORD("Current Password Is Wrong"),
    REQUEST_HAS_NOT_TOKEN("Request Has Not Token"),
    TOKEN_IS_INVALID("Token Is Invalid"),
    NO_USER_ROLE_BY_USER_ID("No User Role By User Id"),
    NEED_PHONE_NUMBER("User Need A Phone Number To Change Multi-Factor Authentication"),
    UNABLE_DIRECTORIES("Unable To Create Directories To Save Image"),
    ERROR_IN_COPY_IMAGE("Error Occured During The Copy Image Action"),
    NO_EVENT_FOUND_BY_EVENT_TYPE("No Found Event By Event Type"),
    NO_CUSTOMER_FOUND_BY_ID("No Found Customer By Id"),
    NO_INVOICE_FOUND_BY_ID("No Invoice Found By Id"),
    NO_FOUND_CUSTOMER_BY_ID("No Found Customer By Id"),
    NO_FOUND_INVOICE_BY_ID("No Found Invoice By Id"),
    NO_STATS_FOUND("No Stats Found"),
    UNABLE_TO_EXPORT_REPORT_FILE("Unable To Export Report File"),
    ID_MUST_NOT_NULL("Id Must Not Be Null");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
