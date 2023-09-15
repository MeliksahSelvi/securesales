package com.meliksah.securesales.service;

import com.meliksah.securesales.domain.AccountVerification;
import com.meliksah.securesales.domain.ResetPasswordVerification;
import com.meliksah.securesales.domain.TwoFactorVerification;
import com.meliksah.securesales.domain.User;
import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.exception.NotFoundException;
import com.meliksah.securesales.repository.AccountVerificationRepository;
import com.meliksah.securesales.repository.ResetPasswordVerificationRepository;
import com.meliksah.securesales.repository.TwoFactorVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {

    private final TwoFactorVerificationRepository twoFactorVerificationRepository;
    private final AccountVerificationRepository accountVerificationRepository;
    private final ResetPasswordVerificationRepository resetPasswordVerificationRepository;

    @Transactional
    public void resetPasswordVerification(User user, String verificationUrl) {

        resetPasswordVerificationRepository.deleteByUserId(user.getId());

        ResetPasswordVerification resetPasswordVerification = createResetPasswordVerification(user, verificationUrl);

        resetPasswordVerificationRepository.save(resetPasswordVerification);
    }

    private ResetPasswordVerification createResetPasswordVerification(User user, String verificationUrl) {

        Date expirationDate = DateUtils.addDays(new Date(), 1);

        ResetPasswordVerification resetPasswordVerification = new ResetPasswordVerification();
        resetPasswordVerification.setUser(user);
        resetPasswordVerification.setExpirationDate(expirationDate);
        resetPasswordVerification.setUrl(verificationUrl);

        return resetPasswordVerification;
    }

    @Transactional
    public void sendVerificationCode(User user) {

        twoFactorVerificationRepository.deleteByUserId(user.getId());

        TwoFactorVerification twoFactorVerification = createTwoFactorVerification(user);

        twoFactorVerificationRepository.save(twoFactorVerification);

        log.info("verification code send to phone: " + twoFactorVerification.getCode());
//        SmsUtil.sendSms(twoFactorVerification.getUser().getPhone(), twoFactorVerification.getCode());
    }

    private TwoFactorVerification createTwoFactorVerification(User user) {
        Date expirationDate = DateUtils.addDays(new Date(), 1);
        String verificationCode = RandomStringUtils.randomAlphabetic(8).toUpperCase();

        TwoFactorVerification twoFactorVerification = new TwoFactorVerification();
        twoFactorVerification.setUser(user);
        twoFactorVerification.setCode(verificationCode);
        twoFactorVerification.setExpirationDate(expirationDate);
        return twoFactorVerification;
    }

    public void saveAccountVerification(User user, String verificationUrl) {

        AccountVerification accountVerification = createAccountVerification(user, verificationUrl);

        accountVerificationRepository.save(accountVerification);
    }

    private AccountVerification createAccountVerification(User user, String verificationUrl) {
        AccountVerification accountVerification = new AccountVerification();
        accountVerification.setUser(user);
        accountVerification.setUrl(verificationUrl);
        return accountVerification;
    }

    public void deleteExVerificationCode(String code) {
        twoFactorVerificationRepository.deleteByCode(code);
    }

    public TwoFactorVerification findTwoFactorVerificationByCode(String code) {
        Optional<TwoFactorVerification> verificationOptional = twoFactorVerificationRepository.findByCode(code);

        return verificationOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_TWO_FACTOR_VERIFICATION_FOUND_BY_CODE);
        });
    }

    public ResetPasswordVerification findPasswordVerificationByUrl(String url) {
        Optional<ResetPasswordVerification> passwordOptional = resetPasswordVerificationRepository.findByUrl(url);

        return passwordOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_PASSWORD_VERIFICATION_FOUND_BY_URL);
        });
    }

    public void deleteExPasswordUrlByUserId(Long userId) {
        resetPasswordVerificationRepository.deleteByUserId(userId);
    }

    public void deleteExAccountVerificationUrl(String url) {
        accountVerificationRepository.deleteByUrl(url);
    }
}
