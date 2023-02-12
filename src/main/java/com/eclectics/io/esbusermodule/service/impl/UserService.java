package com.eclectics.io.esbusermodule.service.impl;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.constants.SystemProcess;
import com.eclectics.io.esbusermodule.model.*;
import com.eclectics.io.esbusermodule.repository.*;
import com.eclectics.io.esbusermodule.service.IUserInterface;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.util.UtilFunctions;
import com.eclectics.io.esbusermodule.workflowService.WorkFlowFilter;
import com.eclectics.io.esbusermodule.wrapper.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;

/**
 * @author Alex Maina
 * @created 25/01/2023
 **/
@Service
@RequiredArgsConstructor
public class UserService implements IUserInterface {
    private final SystemUserRepository systemUserRepository;
    private final Gson gson;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRolesRepository;
    private final RoleRepository roleRepository;
    private final MessageTemplateRepository messageTemplateRepository;

    /**
     *  Create System Admin
     *
     * @param createUserWrapper UserWrapper
     * @return Universal response of success if user is saved
     */
    @WorkFlowFilter(processName = SystemProcess.CREATE_ADMIN)
    @Override
    public Mono<UniversalResponse> createSystemUser(CreateUserWrapper createUserWrapper) {
        return Mono.fromCallable (() -> {
            if (systemUserRepository.findTopByEmailAndSoftDeleteFalse (createUserWrapper.getEmail ()).isPresent ()) {
                return UniversalResponse.builder ().status (400)
                        .message ("User already exists by email " + createUserWrapper.getEmail ()).build ();
            }
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (createUserWrapper.getProfileId ())
                    .orElse (null);

            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            String password = UtilFunctions.generate8CharactersComplexPassword ();
            SystemUser users = SystemUser.builder ()
                    .firstName (createUserWrapper.getFirstName ().toUpperCase ())
                    .lastName (createUserWrapper.getLastName ().toUpperCase ())
                    .email (createUserWrapper.getEmail ().toLowerCase ())
                    .firstTimeLogin (true)
                    .password (passwordEncoder.encode (password))
                    .profile (profile)
                    .build ();

            String message = getMessageTemplateByType (MessageType.WELCOME_ADMIN_MESSAGE).getMessage ().replace ("@name", createUserWrapper.getFirstName ())
                    .replace ("@password", password);

            notificationService.sendEmailNotificationMessage (message, users.getEmail (), "ACCOUNT REGISTRATION");
            systemUserRepository.save (users);
            return UniversalResponse.builder ().status (200).message ("System admin created successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     * @param messageType Message type that will be loaded
     * @return Configured message Template or Default Template if no template is found
     */
    public MessageTemplate getMessageTemplateByType(MessageType messageType) {
        return messageTemplateRepository.findByMessageTypeAndActiveTrueAndSoftDeleteFalse (messageType)
                .orElse (messageTemplateRepository.findByMessageTypeAndDefaultTemplateTrueAndSoftDeleteFalse (messageType));

    }

    @Override
    public Mono<Profile> getUserProfile(String username) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByEmailAndSoftDeleteFalse (username)
                    .orElse (null);
            if (systemUser == null)
                throw new IllegalStateException ("User does not have a profile");

            return systemUser.getProfile ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    @Override
    public Mono<SystemUser> getSystemUserByUsername(String username) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByEmailAndSoftDeleteFalse (username)
                    .orElse (null);
            if (systemUser == null)
                throw new IllegalStateException ("User does not exist");

            return systemUser;
        }).publishOn (Schedulers.boundedElastic ());
    }

    @WorkFlowFilter(processName = SystemProcess.UPDATE_ADMIN)
    @Override
    public Mono<UniversalResponse> updateUser(UpdateUserWrapper updateUserWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository
                    .findTopByIdAndSoftDeleteFalse (updateUserWrapper.getId ()).orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found").build ();
            }
            if (updateUserWrapper.getFirstName () != null) {
                systemUser.setFirstName (updateUserWrapper.getFirstName ());
            }
            if (updateUserWrapper.getLastName () != null) {
                systemUser.setLastName (updateUserWrapper.getLastName ());
            }
            if (updateUserWrapper.getProfileId () != null) {
                Profile profile = profileRepository.findByIdAndSoftDeleteFalse (updateUserWrapper.getProfileId ())
                        .orElse (null);
                if (profile == null) {
                    return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
                }
                systemUser.setProfile (profile);
            }
            systemUserRepository.save (systemUser);
            return UniversalResponse.builder ().status (200).message ("User updated successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    @Override
    public Mono<UniversalResponse> resetUser(ResetUserPassword resetUserPassword) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository
                    .findTopByIdAndSoftDeleteFalse (resetUserPassword.getUserId ()).orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found").build ();
            }
            String newPassword = UtilFunctions.generate8CharactersComplexPassword ();
            String message = getMessageTemplateByType (MessageType.RESET_ADMIN_MESSAGE).getMessage ()
                    .replace ("@name", systemUser.getEmail ())
                    .replace ("@password", newPassword);
            systemUser.setPassword (passwordEncoder.encode (newPassword));
            systemUser.setPasswordResetAt (new Date ());
            systemUserRepository.save (systemUser);
            notificationService.sendEmailNotificationMessage (message, systemUser.getEmail (), "ACCOUNT RESET");
            return UniversalResponse.builder ().status (200).message ("User reset successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> updateUserPassword(UpdatePasswordWrapper passwordWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByIdAndSoftDeleteFalse (passwordWrapper.getId ())
                    .orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found").build ();
            }
            if (!passwordEncoder.matches (passwordWrapper.getOldPassword (), systemUser.getPassword ())) {
                if (systemUser.getResetPasswordLoginCount () > 2) {
                    systemUser.setBlockedBy ("SYSTEM");
                    systemUser.setBlockedRemarks ("Wrong old password exceeding 3 times ");
                    systemUser.setBlocked (true);
                    systemUserRepository.save (systemUser);
                }
                return UniversalResponse.builder ().status (400).message ("Invalid old pin").build ();
            }
            systemUser.setPassword (passwordEncoder.encode (passwordWrapper.getNewPassword ()));
            systemUserRepository.save (systemUser);
            return UniversalResponse.builder ().status (200).message ("Password update successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> getUserById(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByIdAndSoftDeleteFalse (commonWrapper.getId ())
                    .orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found")
                        .build ();
            }
            return UniversalResponse.builder ().status (200).message ("User information retrieved successfully")
                    .data (systemUser).build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> getAllUsers(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            Pageable pageable = PageRequest.of (commonWrapper.getPage (), commonWrapper.getSize ());
            Page<SystemUser> systemUsers = switch (commonWrapper.getFilter ()) {
                case "all" -> systemUserRepository.findAllBySoftDeleteFalse (pageable);
                case "active" -> systemUserRepository.findAllByBlockedFalseAndSoftDeleteFalse (pageable);
                case "inactive" -> systemUserRepository.findAllByBlockedTrueAndSoftDeleteFalse (pageable);
                default -> Page.empty ();
            };

            return UniversalResponse.builder ().status (200).message ("System administrators").data (systemUsers.toList ())
                    .totalItems ((int) systemUsers.getTotalElements ()).build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.DELETE_ADMIN)
    @Override
    public Mono<UniversalResponse> deleteUser(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByIdAndSoftDeleteFalse (commonWrapper.getId ())
                    .orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("system administrator not found").build ();
            }
            systemUser.setBlocked (true);
            systemUser.setSoftDelete (true);
            systemUser.setRemarks (commonWrapper.getRemarks ());
            systemUserRepository.save (systemUser);
            return UniversalResponse.builder ().status (200).message ("System administrator deleted successful")
                    .build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.DISABLE_ADMIN)
    @Override
    public Mono<UniversalResponse> disableUser(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByIdAndSoftDeleteFalse (commonWrapper.getId ())
                    .orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found").build ();
            }
            systemUser.setBlocked (true);
            systemUser.setRemarks (commonWrapper.getRemarks ());
            systemUserRepository.save (systemUser);
            return UniversalResponse.builder ().message ("System administrator disabled successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.ENABLE_ADMIN)
    @Override
    public Mono<UniversalResponse> enableUser(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            SystemUser systemUser = systemUserRepository.findTopByIdAndSoftDeleteFalse (commonWrapper.getId ())
                    .orElse (null);
            if (systemUser == null) {
                return UniversalResponse.builder ().status (400).message ("User not found").build ();
            }
            systemUser.setBlocked (false);
            systemUser.setRemarks (commonWrapper.getRemarks ());
            systemUserRepository.save (systemUser);
            return UniversalResponse.builder ().status (200).message ("System administrator enabled successful")
                    .build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.CREATE_PROFILE)
    @Override
    public Mono<UniversalResponse> createProfile(CreateProfileWrapper profileWrapper) {
        return Mono.fromCallable (() -> {
            if (profileRepository.findByNameAndSoftDeleteFalse (profileWrapper.getName ()).isPresent ()) {
                return UniversalResponse.builder ().status (400).message ("Profile already exists").build ();
            }
            Profile profile = Profile.builder ()
                    .name (profileWrapper.getName ().toLowerCase ())
                    .remarks (profileWrapper.getRemarks ()).build ();
            profileRepository.save (profile);
            return UniversalResponse.builder ().status (200).message ("Profile saved successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.UPDATE_PROFILE)
    @Override
    public Mono<UniversalResponse> updateProfile(CommonProfileWrapper profileWrapper) {
        return Mono.fromCallable (() -> {
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (profileWrapper.getId ())
                    .orElse (null);
            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            profile.setRemarks (profile.getRemarks ());
            profileRepository.save (profile);
            return UniversalResponse.builder ().status (200).message ("Profile updated successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.DELETE_PROFILE)
    @Override
    public Mono<UniversalResponse> archiveProfile(CommonProfileWrapper commonProfileWrapper) {
        return Mono.fromCallable (() -> {
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (commonProfileWrapper.getId ())
                    .orElse (null);
            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            if (systemUserRepository.existsByProfileIdAndSoftDeleteFalse (commonProfileWrapper.getId ())) {
                return UniversalResponse.builder ().status (400)
                        .message ("Cannot archive profile, profile is assigned to users'").build ();
            }
            profile.setSoftDelete (true);
            profileRepository.save (profile);
            return UniversalResponse.builder ().status (200).message ("Profile archived successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    @Override
    public Mono<UniversalResponse> getProfile(CommonProfileWrapper commonProfileWrapper) {
        return Mono.fromCallable (() -> {
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (commonProfileWrapper.getId ())
                    .orElse (null);
            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            List<CommonRoleWrapper> profileRolesList = profileRolesRepository
                    .findAllByProfileIdAndSoftDeleteFalse (profile.getId ()).stream ()
                    .map (profileRoles -> CommonRoleWrapper.builder ()
                            .roleId (profileRoles.getRole ().getId ())
                            .name (profileRoles.getRole ().getName ())
                            .systemRole (profileRoles.getRole ().isSystemRole ())
                            .build ()).toList ();

            return UniversalResponse.builder ().status (200)
                    .message ("profile info")
                    .data (Map.of ("profile", profile, "roles", profileRolesList)).build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> getProfiles(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            Pageable pageable = PageRequest.of (commonWrapper.getPage (), commonWrapper.getSize ());
            Page<Profile> profilesPage = profileRepository.findAllBySoftDeleteFalse (pageable);
            return UniversalResponse.builder ().status (200).message ("Profile list")
                    .data (profilesPage.toList ()).totalItems (profilesPage.getNumberOfElements ()).build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> getAllRoles(CommonWrapper commonWrapper) {
        return Mono.fromCallable (() -> {
            Pageable pageable= PageRequest.of (commonWrapper.getPage (), commonWrapper.getSize ());
            Page<Role> roles = roleRepository.findAllBySoftDeleteFalse (pageable);
            return UniversalResponse.builder ().status (200).message ("All roles").data (roles.toList ())
                    .totalItems ((int)roles.getTotalElements ())
                    .build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.DELETE_ROLE)
    @Override
    public Mono<UniversalResponse> deleteRole(CommonRoleWrapper roleWrapper) {
        return Mono.fromCallable (()-> {
            Role role= roleRepository.findByIdAndSoftDeleteFalse (roleWrapper.getRoleId ())
                    .orElse (null);
            if(role==null)
                return UniversalResponse.builder ().status (400).message ("Role not found").build ();
            if(role.isSystemRole ()){
                return UniversalResponse.builder ().status (400).message ("Cannot delete a system role").build ();
            }
            role.setSoftDelete (true);
            roleRepository.save (role);
            return UniversalResponse.builder ().status (200).message ("Role saved successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    @WorkFlowFilter (processName = SystemProcess.CREATE_ROLE)
    @Override
    public Mono<UniversalResponse> addRole(CommonRoleWrapper roleWrapper) {
        return Mono.fromCallable (() -> {
            if (roleRepository.findByNameAndSoftDeleteFalse (roleWrapper.getName ()).isPresent ()) {
                return UniversalResponse.builder ().status (400).message ("Role by name " + roleWrapper.getName ()
                        + " already exists").build ();
            }
            Role role = Role.builder ()
                    .isSystemRole (false)
                    .remarks (roleWrapper.getRemarks ())
                    .name (UtilFunctions.capitalizeAndRemoveSpaces ().apply (roleWrapper.getName ().toUpperCase ()))
                    .build ();
            roleRepository.save (role);
            return UniversalResponse.builder ().status (200).message ("Role saved successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }

    @WorkFlowFilter (processName = SystemProcess.ASSIGN_ROLE_TO_PROFILE)
    @Override
    public Mono<UniversalResponse> addRolesToProfile(CommonRoleWrapper commonRoleWrapper) {
        return Mono.fromCallable (() -> {
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (commonRoleWrapper.getProfileId ())
                    .orElse (null);
            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            StringJoiner invalidRoles = new StringJoiner (",", "[", "]");
            commonRoleWrapper.getRoleList ()
                    .forEach (roleId -> {
                        Role role = roleRepository.findByIdAndSoftDeleteFalse (roleId)
                                .orElse (null);
                        if (role == null) {
                            invalidRoles.add (String.format ("Role by id %s does not exists", roleId));
                        } else if (profileRolesRepository.existsByProfileIdAndRoleIdAndSoftDeleteFalse (profile.getId (), roleId)) {
                            invalidRoles.add (String.format ("Role by id %s already exists in profile", roleId));
                        } else {
                            ProfileRoles profileRoles = ProfileRoles.builder ()
                                    .profile (profile)
                                    .role (role)
                                    .build ();
                            profileRolesRepository.save (profileRoles);
                        }
                    });
            if (invalidRoles.length () > 3) {
                return UniversalResponse.builder ().status (200)
                        .message ("Added roles but found errors:  " + invalidRoles).build ();
            }
            return UniversalResponse.builder ().status (200).message ("Added roles to profile successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.REMOVE_ROLE_FROM_PROFILE)
    @Override
    public Mono<UniversalResponse> removeRolesFromProfile(CommonRoleWrapper commonRoleWrapper) {
        return Mono.fromCallable (() -> {
            Profile profile = profileRepository.findByIdAndSoftDeleteFalse (commonRoleWrapper.getProfileId ())
                    .orElse (null);
            if (profile == null) {
                return UniversalResponse.builder ().status (400).message ("Profile not found").build ();
            }
            StringJoiner invalidRoles = new StringJoiner (",", "[", "]");
            commonRoleWrapper.getRoleList ()
                    .forEach (roleId -> {
                        ProfileRoles profileRoles = profileRolesRepository.findByProfileIdAndRoleIdAndSoftDeleteFalse (profile.getId (), roleId)
                                .orElse (null);
                        if (profileRoles == null) {
                            invalidRoles.add (String.format ("Role by id %s not found in profile", roleId));
                        } else {
                            profileRoles.setSoftDelete (true);
                        }
                    });
            if (invalidRoles.length () > 3) {
                return UniversalResponse.builder ().status (200)
                        .message ("Removed roles but found errors:  " + invalidRoles).build ();
            }
            return UniversalResponse.builder ().status (200).message ("Removed roles successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @WorkFlowFilter (processName = SystemProcess.ADD_MESSAGE_TEMPLATE)
    @Override
    @SuppressWarnings ("UnstableApiUsage")
    public Mono<UniversalResponse> addMessageTemplate(MessageTemplate messageTemplate) {
        return Mono.fromCallable (() -> {
            MessageTemplate oldMessageTemplate = messageTemplateRepository
                    .findByMessageTypeAndActiveTrueAndSoftDeleteFalse (messageTemplate.getMessageType ())
                    .orElse (messageTemplateRepository
                            .findByMessageTypeAndDefaultTemplateTrueAndSoftDeleteFalse (messageTemplate.getMessageType ()));
            if (oldMessageTemplate == null) {
                return UniversalResponse.builder ().status (400).message ("Invalid Message type passed").build ();
            }
            String newMessage = messageTemplate.getMessage ();
            List<String> params =
                    gson.fromJson (oldMessageTemplate.getBaseParams (), new TypeToken<List<String>> () {
                    }.getType ());
            StringJoiner stringJoiner = new StringJoiner (",");
            params.forEach (param -> {
                if (!newMessage.contains (param)) {
                    stringJoiner.add (param);
                }
            });

            if (stringJoiner.length () > 1) {
                return UniversalResponse.builder ().status (400).message ("Missing following template params " + stringJoiner)
                        .build ();
            }
            MessageTemplate newTemplate = MessageTemplate.builder ()
                    .message (messageTemplate.getMessage ())
                    .baseParams (oldMessageTemplate.getBaseParams ())
                    .active (true)
                    .messageType (messageTemplate.getMessageType ())
                    .build ();
            oldMessageTemplate.setActive (false);
            messageTemplateRepository.saveAll (List.of (newTemplate, oldMessageTemplate));
            return UniversalResponse.builder ().status (200).message ("Message template updated successfully").build ();
        }).publishOn (Schedulers.boundedElastic ());
    }
    @Override
    public Mono<UniversalResponse> getMessageTypes() {
        return Mono.just (UniversalResponse.builder ().status (200).message ("Message types")
                .data (MessageType.values ()).build ()).cache (Duration.ofMinutes (3));
    }
    @Override
    public Mono<UniversalResponse> getMessageTemplates(){
        return Mono.fromCallable (()-> {
            List<MessageTemplate> messageTemplates= new ArrayList<> ();
            Arrays.asList (MessageType.values ()).forEach (type->{
                MessageTemplate messageTemplate=getMessageTemplateByType (type);
                if(messageTemplate!=null){
                    messageTemplates.add (messageTemplate);
                }
            });
            return UniversalResponse.builder().status (200).message ("Message templates")
                    .data (messageTemplates).build();
        }).cache (Duration.ofMinutes (3));
    }

}
