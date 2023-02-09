package com.eclectics.io.esbusermodule.UserServiceTests;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.model.MessageTemplate;
import com.eclectics.io.esbusermodule.model.Profile;
import com.eclectics.io.esbusermodule.model.SystemUser;
import com.eclectics.io.esbusermodule.repository.*;
import com.eclectics.io.esbusermodule.service.impl.NotificationService;
import com.eclectics.io.esbusermodule.service.impl.UserService;
import com.eclectics.io.esbusermodule.wrapper.CreateUserWrapper;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Alex Maina
 * @created 07/02/2023
 **/
@SpringBootTest
class UserServiceTest {
    @MockBean
    private SystemUserRepository systemUserRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private ProfileRepository profileRepository;
    @MockBean
    private ProfileRoleRepository profileRolesRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private MessageTemplateRepository messageTemplateRepository;
    @Autowired
    private UserService userService;
    private AutoCloseable openMocks;
    private SystemUser systemUser;
    private Profile profile;
    private MessageTemplate welcomeMessageTemplate ;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks (this);
        profile = Profile.builder ()
                .name ("ADMIN")
                .build ();
        systemUser = SystemUser.builder ()
                .userName ("maina.alex@eclectics.io")
                .email ("maina.alex@eclectics.io")
                .firstName ("Alex")
                .lastName ("Maina")
                .blocked (false)
                .firstTimeLogin (true)
                .build ();
         welcomeMessageTemplate=MessageTemplate.builder ()
                .message (" Dear @name, you have been added as an ECLECTICS ESB Administrator" +
                        ".Use your email and password @password to login. %n Kind Regards %n ECLECTICS ESB TEAM")
                .baseParams (new Gson ().toJson (List.of ("@name", "@password")))
                .active (true)
                .messageType (MessageType.WELCOME_ADMIN_MESSAGE)
                .build ();
    }

    @Test
   void createSystemUserTests_withUserExists_returnUniversalResponseWithStatusCode400() {
        when (systemUserRepository.findTopByEmailAndSoftDeleteFalse (anyString ()))
                .thenReturn (Optional.of (systemUser));
        CreateUserWrapper createUserWrapper = new CreateUserWrapper ();
        createUserWrapper.setEmail ("maina.alex@eclectics.io");
        createUserWrapper.setLastName ("Alex");
        createUserWrapper.setFirstName ("Maina");
        createUserWrapper.setProfileId (1);
        StepVerifier.create (userService.createSystemUser (createUserWrapper))
                .assertNext (response-> {
                    Assertions.assertThat (response.getStatus ()).isEqualTo (400);
                    Assertions.assertThat (response.getMessage ())
                            .isEqualTo ("User already exists by email " + createUserWrapper.getEmail ());
                })
                .expectComplete ()
                .verify ();
    }

    @Test
     void createSystemUserTest_withProfileIdNotExist_returnsUniversalResponseWithStatusCode400(){
        when (systemUserRepository.findTopByEmailAndSoftDeleteFalse (anyString ()))
                .thenReturn (Optional.empty ());
        when(profileRepository.findByIdAndSoftDeleteFalse (anyLong ()))
                .thenReturn (Optional.empty ());
        CreateUserWrapper createUserWrapper = new CreateUserWrapper ();
        createUserWrapper.setEmail ("maina.alex@eclectics.io");
        createUserWrapper.setLastName ("Alex");
        createUserWrapper.setFirstName ("Maina");
        createUserWrapper.setProfileId (1);

        StepVerifier.create (userService.createSystemUser (createUserWrapper))
                .assertNext (response-> {
                    assertThat (400).isEqualTo (response.getStatus ());
                    assertThat ("Profile not found").isEqualTo (response.getMessage ());
                }).verifyComplete ();

    }

    @Test
    void createSystemUser_validInput_returnsSuccessResponse(){
        when (systemUserRepository.findTopByEmailAndSoftDeleteFalse (anyString ()))
                .thenReturn (Optional.empty ());
        when(profileRepository.findByIdAndSoftDeleteFalse (anyLong ()))
                .thenReturn (Optional.of (profile));
        CreateUserWrapper createUserWrapper = new CreateUserWrapper ();
        when(userService.getMessageTemplateByType (MessageType.WELCOME_ADMIN_MESSAGE))
                .thenReturn (welcomeMessageTemplate);
        createUserWrapper.setEmail ("maina.alex@eclectics.io");
        createUserWrapper.setLastName ("Alex");
        createUserWrapper.setFirstName ("Maina");
        createUserWrapper.setProfileId (1);
        StepVerifier.create (userService.createSystemUser (createUserWrapper))
                .assertNext (res-> {
                    assertThat (res.getStatus ()).isEqualTo (200);
                    assertThat (res.getMessage ()).isEqualTo ("System admin created successfully");
                }).verifyComplete ();
    }


    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close ();
    }


}
