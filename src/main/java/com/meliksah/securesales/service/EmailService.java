package com.meliksah.securesales.service;

import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.enumeration.VerificationType;
import com.meliksah.securesales.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationUrl(String firstName, String email, String verificationUrl, VerificationType verificationType) {

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("");
            mail.setTo(email);
            mail.setText(getEmailMessage(firstName, verificationUrl, verificationType));
            mail.setSubject(String.format("SecureSale - %s Verification Email", StringUtils.capitalize(verificationType.toString())));
            javaMailSender.send(mail);
            log.info("Email sent to {}: ", firstName);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch (verificationType) {
            case PASSWORD -> {
                return "Hello " + firstName + "\n\nReset password request. Please click the link below to reset your password.\n\n" + verificationUrl + "\n\nThe Support Team";
            }
            case ACCOUNT -> {
                return "Hello " + firstName + "\n\nYour new account has been created. Please click the link below to verify your account.\n\n" + verificationUrl + "\n\nThe Support Team";
            }
            default -> throw new ApiException(ErrorMessage.EMAIL_TYPE_KNOWN);
        }
    }
}
