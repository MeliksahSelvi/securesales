package com.meliksah.securesales.utils;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

public class Queries {

    public final static String USER_BY_EMAIL = "Select user From User user where user.email =:email";

    public final static String ROLE_BY_ROLETYPE = "Select role FROM Role role where role.roleType=:roleType";

    public final static String ROLE_BY_USER_ID = "Select userRole.role FROM UserRole userRole where userRole.user.id =:userId";

    public final static String ROLE_BY_EMAIL = "Select userRole.role FROM UserRole userRole where userRole.user.email =:email";

    public final static String DELETE_CODE_BY_USER_ID = "DELETE FROM TwoFactorVerification twoFactor where twoFactor.user.id =:userId";

    public final static String DELETE_CODE = "DELETE FROM TwoFactorVerification twoFactor where twoFactor.code =:code";

    public final static String DELETE_ACCOUNT_VERIFICATION_URL = "Delete from AccountVerification accVerify where accVerify.url=:url";

    public final static String USER_BY_EMAIL_AND_TWO_FACTOR_CODE = "Select user From User user where user.email =:email and user.id=(Select twoFactor.user.id From TwoFactorVerification twoFactor where twoFactor.code=:code)";

    public final static String USER_BY_PASSWORD_URL = "Select user From User user where user.id=(Select passVerify.user.id From ResetPasswordVerification passVerify where passVerify.url=:url)";

    public final static String USER_BY_ACCOUNT_URL = "Select user From User user where user.id=(Select accVerify.user.id From AccountVerification accVerify where accVerify.url=:url)";

    public final static String TWO_FACTOR_CODE_BY_CODE = "Select twoFactor from TwoFactorVerification twoFactor where twoFactor.code =:code";

    public final static String DELETE_PASSWORD_VERIFICATION_BY_USER_UD = " DELETE FROM ResetPasswordVerification passwordVerification where passwordVerification.user.id=:userId";

    public final static String PASSWORD_VERIFICATION_CODE_BY_URL = "Select resetPassword From ResetPasswordVerification resetPassword where resetPassword.url=:url";

    public static final String USER_ROLE_BY_USER_UD = "Select userRole From UserRole userRole where userRole.user.id=:userId";

    public static final String EVENT_DTO_BY_USER_ID = "Select userEvent.id,event.eventType,event.description,userEvent.device,userEvent.ipAddress,userEvent.createdAt From UserEvent userEvent where userEvent.user.id=:userId";

    public static final String EVENT_BY_EVENT_TYPE = "Select event From Event event where event.eventType=:eventType";

    public static final String CUSTOMERS_BY_NAME = "Select customer From Customer customer where customer.name=:name";

    public static final String CUSTOMER_BY_EMAIL = "Select customer From Customer customer where customer.email=:email";

    public static final String GET_STATS = "Select " +
            "(select count(cus) From Customer cus) as totalCustomers, " +
            "count(inv) as totalInvoices, " +
            "round(sum(inv.total),2) as totalBilled From Invoice inv";
}
