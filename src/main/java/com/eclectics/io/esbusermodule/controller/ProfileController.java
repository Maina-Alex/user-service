package com.eclectics.io.esbusermodule.controller;

import com.eclectics.io.esbusermodule.service.IUserInterface;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.wrapper.CommonProfileWrapper;
import com.eclectics.io.esbusermodule.wrapper.CommonRoleWrapper;
import com.eclectics.io.esbusermodule.wrapper.CommonWrapper;
import com.eclectics.io.esbusermodule.wrapper.CreateProfileWrapper;
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
 * @created 08/02/2023
 **/
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final IUserInterface userInterface;

    @PostMapping("/add")
    public Mono<ResponseEntity<UniversalResponse>> addProfile(@Valid @RequestBody CreateProfileWrapper createProfileWrapper){
        return userInterface.createProfile (createProfileWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/edit")
    public Mono<ResponseEntity<UniversalResponse>> editProfile(@Valid @RequestBody CommonProfileWrapper profileWrapper){
        return userInterface.updateProfile (profileWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteProfile(@RequestBody CommonProfileWrapper commonProfileWrapper ){
        return userInterface.archiveProfile (commonProfileWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get")
    public Mono<ResponseEntity<UniversalResponse>> addRoleToProfile(CommonProfileWrapper commonProfileWrapper){
        return userInterface.getProfile (commonProfileWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get/all")
    public Mono<ResponseEntity<UniversalResponse>> getProfiles(CommonWrapper commonWrapper){
        return userInterface.getProfiles (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/add/roles")
    public  Mono<ResponseEntity<UniversalResponse>> addRolesToProfile(CommonRoleWrapper commonRoleWrapper){
        return userInterface.addRolesToProfile (commonRoleWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/remove/roles")
    public Mono<ResponseEntity<UniversalResponse>> removeRolesFromProfile(CommonRoleWrapper commonRoleWrapper){
        return  userInterface.removeRolesFromProfile (commonRoleWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }
}
