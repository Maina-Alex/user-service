package com.eclectics.io.esbusermodule.controller;

import com.eclectics.io.esbusermodule.service.IUserInterface;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.wrapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

/**
 * @author Alex Maina
 * @created 07/02/2023
 **/
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserInterface userInterface;

    @PostMapping("/signup")
    public Mono<ResponseEntity<UniversalResponse>> createAdministrator(@Valid @RequestBody CreateUserWrapper createUserWrapper){
        return userInterface.createSystemUser (createUserWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/update-user")
    public Mono<ResponseEntity<UniversalResponse>> updateAdministrator(@RequestBody UpdateUserWrapper updateUserWrapper){
        return userInterface.updateUser (updateUserWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/forget-password")
    public Mono<ResponseEntity<UniversalResponse>> resetAdministrator(@Valid @RequestBody ResetUserPassword resetUserPassword){
        return userInterface.resetUser (resetUserPassword)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/set-password")
    public Mono<ResponseEntity<UniversalResponse>> updateUserPassword(@Valid @RequestBody UpdatePasswordWrapper updatePasswordWrapper){
        return userInterface.updateUserPassword (updatePasswordWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get-user-details")
    public Mono<ResponseEntity<UniversalResponse>> getUserById(@RequestBody CommonWrapper commonWrapper){
        return userInterface.getUserById (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/list-users")
    public Mono<ResponseEntity<UniversalResponse>> getAllUsers(@RequestBody CommonWrapper commonWrapper){
        return userInterface.getAllUsers (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteUser(@RequestBody CommonWrapper commonWrapper){
        return userInterface.deleteUser (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/block-user")
    public Mono<ResponseEntity<UniversalResponse>> disableUser(CommonWrapper commonWrapper){
        return userInterface.disableUser (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/unblock-user")
    public Mono<ResponseEntity<UniversalResponse>> enableUser(CommonWrapper commonWrapper){
        return userInterface.enableUser (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }
}
