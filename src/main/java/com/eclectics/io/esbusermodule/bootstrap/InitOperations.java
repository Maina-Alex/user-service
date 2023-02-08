package com.eclectics.io.esbusermodule.bootstrap;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.model.MessageTemplate;
import com.eclectics.io.esbusermodule.repository.MessageTemplateRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitOperations {
    private final MessageTemplateRepository messageTemplateRepository;
    private final Gson gson;

    @PostConstruct
    public void init(){
         Mono.fromRunnable (()-> {
             saveDefaultMessageTemplates();
         }).subscribeOn (Schedulers.boundedElastic ())
                 .subscribe (null,err-> log.error ("An error occurred during initialization e=> {}",
                         err.getLocalizedMessage ()),null);
    }

    private void saveDefaultMessageTemplates(){
        if(messageTemplateRepository.findByMessageTypeAndDefaultTemplateTrueAndSoftDeleteFalse (MessageType.WELCOME_ADMIN_MESSAGE)==null) {
            MessageTemplate welcomeMessageTemplate = MessageTemplate.builder ()
                    .message (" Dear @name, you have been added as an ECLECTICS ESB Administrator" +
                            ".Use your email and password @password to login. %n Kind Regards %n ECLECTICS ESB TEAM")
                    .baseParams (gson.toJson (List.of ("@name", "@password")))
                    .active (true)
                    .messageType (MessageType.WELCOME_ADMIN_MESSAGE)
                    .build ();
            messageTemplateRepository.save(welcomeMessageTemplate);
        }
        if(messageTemplateRepository.findByMessageTypeAndDefaultTemplateTrueAndSoftDeleteFalse (MessageType.RESET_ADMIN_MESSAGE)==null) {
            MessageTemplate resetAdminMessageTemplate = MessageTemplate.builder ()
                    .message ("Dear @name, your account has been reset. User new password @password to login. %n Kind Regards %n ECLECTICS ESB TEAM ")
                    .baseParams (gson.toJson (List.of ("@name", "@password")))
                    .active (true)
                    .messageType (MessageType.RESET_ADMIN_MESSAGE)
                    .build ();

            messageTemplateRepository.save(resetAdminMessageTemplate);
        }
    }


}
