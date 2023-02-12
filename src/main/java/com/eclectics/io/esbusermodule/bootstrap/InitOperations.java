package com.eclectics.io.esbusermodule.bootstrap;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.model.*;
import com.eclectics.io.esbusermodule.repository.*;
import com.eclectics.io.esbusermodule.service.impl.NotificationService;
import com.eclectics.io.esbusermodule.util.UtilFunctions;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitOperations {
    @Value("${default.admin.email}")
    private String adminEmail;

    private final MessageTemplateRepository messageTemplateRepository;
    private final Gson gson;
    private final SystemUserRepository systemUserRepository;
    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final RoleRepository roleRepository;
    private static final String SYSTEM_ADMIN_PROFILE_NAME = "SUPER-ADMIN";
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;


    @PostConstruct
    public void init() {
        Mono.fromRunnable (() -> {
                    saveDefaultMessageTemplates ();
                    createSystemSuperProfile ();
                    saveDefaultUser();
                }).subscribeOn (Schedulers.boundedElastic ())
                .subscribe (null, err -> log.error ("An error occurred during initialization e=> {}",
                        err.getLocalizedMessage ()), null);
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

            messageTemplateRepository.save (resetAdminMessageTemplate);
        }
    }

    public void createSystemSuperProfile() {
        if (profileRepository.findByNameAndSoftDeleteFalse (SYSTEM_ADMIN_PROFILE_NAME).isEmpty ()) {

            Profile profile = profileRepository.save (Profile
                    .builder ()
                    .name (SYSTEM_ADMIN_PROFILE_NAME)
                    .remarks ("SUPER ADMIN PROFILE")
                    .build ());

            roleRepository.findAllBySoftDeleteFalse ()
                    .stream ()
                    .map (role -> roleProfileMapper ().apply (profile, role))
                    .forEach (profileRoleRepository::save);
        }
    }

    BiFunction<Profile, Role, ProfileRoles> roleProfileMapper() {
        return (profile, role) -> {
            ProfileRoles profileRoles = ProfileRoles.builder ()
                    .role (role)
                    .profile (profile)
                    .build ();
            return profileRoleRepository.save (profileRoles);
        };
    }

    public void saveDefaultUser() {
        if (systemUserRepository.findTopByEmailAndSoftDeleteFalse (adminEmail).isEmpty ()) {
            Profile profile = profileRepository.findByNameAndSoftDeleteFalse (SYSTEM_ADMIN_PROFILE_NAME)
                    .orElse (null);
            if (profile != null) {
                String password = UtilFunctions.generate8CharactersComplexPassword ();
                SystemUser systemUser = SystemUser.builder ()
                        .email (adminEmail)
                        .userName (adminEmail)
                        .profile (profile)
                        .firstName ("ESB-SYSTEM-ADMIN")
                        .lastName ("ESB-SYSTEM-ADMIN")
                        .password (passwordEncoder.encode (password))
                        .blocked (false)
                        .firstTimeLogin (true)
                        .loginAttempts (0)
                        .build ();
                String message= String.format ("Dear ESB SUPER ADMINISTRATOR , Use your email and first time pin  %s to login." +
                        " %n Kind Regards %n ECLECTICS ESB TEAM",password);
                systemUserRepository.save (systemUser);
                notificationService.sendEmailNotificationMessage (message,adminEmail,"SUPER ADMIN ACCOUNT CREDENTIALS");
            }
        }
    }


}
