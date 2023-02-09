package com.eclectics.io.esbusermodule.controller;

import com.eclectics.io.esbusermodule.model.MessageTemplate;
import com.eclectics.io.esbusermodule.service.IUserInterface;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

/**
 * @author Alex Maina
 * @created 08/02/2023
 **/
@RestController
@RequestMapping("/api/v1/template")
@RequiredArgsConstructor
public class MessageTemplateController {
    private final IUserInterface userInterface;

    @PostMapping("/types")
    public Mono<ResponseEntity<UniversalResponse>> getTemplatesTypes(){
        return userInterface.getMessageTypes ()
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get")
    public Mono<ResponseEntity<UniversalResponse>> getTemplates(){
        return userInterface.getMessageTemplates ()
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<UniversalResponse>> addTemplate(MessageTemplate messageTemplate){
        return userInterface.addMessageTemplate (messageTemplate)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }


}
