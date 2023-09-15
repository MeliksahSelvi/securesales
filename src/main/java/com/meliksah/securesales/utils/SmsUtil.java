package com.meliksah.securesales.utils;

import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.RestException;
import com.textmagic.sdk.resource.instance.TMNewMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

@Slf4j
public class SmsUtil {

    public static void sendSms(String telephone,String code) {
        CompletableFuture.runAsync(() -> {
            RestClient client = new RestClient("melik", "APIV2_TOKEN");

            TMNewMessage message = client.getResource(TMNewMessage.class);
            message.setText(code);
            message.setPhones(List.of("+905366002340", telephone));
            try {
                message.send();
            } catch (RestException e) {
                throw new RuntimeException(e);
            }
            log.info("Code send it -> {}", message.getId());
        });
    }
}
