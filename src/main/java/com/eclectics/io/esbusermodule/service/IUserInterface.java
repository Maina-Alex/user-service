package com.eclectics.io.esbusermodule.service;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.model.MessageTemplate;
import com.eclectics.io.esbusermodule.model.Profile;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.wrapper.*;
import reactor.core.publisher.Mono;

/**
 * @author Alex Maina
 * @created 08/02/2023
 **/
public interface IUserInterface {
    Mono<UniversalResponse> createSystemUser(CreateUserWrapper createUserWrapper);
    Mono<UniversalResponse> updateUser(UpdateUserWrapper updateUserWrapper);
    Mono<UniversalResponse> resetUser(ResetUserPassword resetUserPassword);
    Mono<UniversalResponse> updateUserPassword(UpdatePasswordWrapper passwordWrapper);
    Mono<UniversalResponse> getUserById(CommonWrapper commonWrapper);
    Mono<UniversalResponse> getAllUsers(CommonWrapper commonWrapper);
    Mono<UniversalResponse> deleteUser(CommonWrapper commonWrapper);
    Mono<UniversalResponse> disableUser(CommonWrapper commonWrapper);
    Mono<UniversalResponse> enableUser(CommonWrapper commonWrapper);
    Mono<UniversalResponse> createProfile(CreateProfileWrapper profileWrapper);
    Mono<UniversalResponse> updateProfile(CommonProfileWrapper profileWrapper);
    Mono<UniversalResponse> archiveProfile(CommonProfileWrapper commonProfileWrapper);
    Mono<UniversalResponse> getProfile(CommonProfileWrapper commonProfileWrapper);
    Mono<UniversalResponse> getProfiles(CommonWrapper commonWrapper);
    Mono<UniversalResponse> getAllRoles(CommonWrapper commonWrapper);
    Mono<UniversalResponse> addRole(CommonRoleWrapper roleWrapper);
    Mono<UniversalResponse> deleteRole(CommonRoleWrapper roleWrapper);
    Mono<UniversalResponse> addRolesToProfile(CommonRoleWrapper commonRoleWrapper);
    Mono<UniversalResponse> removeRolesFromProfile(CommonRoleWrapper commonRoleWrapper);
    Mono<UniversalResponse> addMessageTemplate(MessageTemplate messageTemplate);
    Mono<UniversalResponse> getMessageTypes();
    Mono<UniversalResponse> getMessageTemplates();
    MessageTemplate getMessageTemplateByType(MessageType messageType);
    Mono<Profile> getUserProfile(String username);
}
