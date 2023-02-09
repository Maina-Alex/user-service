package com.eclectics.io.esbusermodule.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final StreamBridge streamBridge;
    private static final String EMAIL_TOPIC= "email-out-0";

    public void sendEmailNotificationMessage(String message, String email, String subject){
        Map<String, String> messageMap= new HashMap<> ();
        messageMap.put ("email",email);
        messageMap.put ("message",message);
        messageMap.put ("subject",subject);
        streamBridge.send(EMAIL_TOPIC, new Gson ().toJson (messageMap));
    }

}
