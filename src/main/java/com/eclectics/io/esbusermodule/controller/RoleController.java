package com.eclectics.io.esbusermodule.controller;

import com.eclectics.io.esbusermodule.service.IUserInterface;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.wrapper.CommonRoleWrapper;
import com.eclectics.io.esbusermodule.wrapper.CommonWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Alex Maina
 * @created 08/02/2023
 **/
@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final IUserInterface userInterface;

    @PostMapping("/add")
    public Mono<ResponseEntity<UniversalResponse>> addRole(CommonRoleWrapper commonRoleWrapper){
        return userInterface.addRole (commonRoleWrapper)
                .map(ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/all")
    public Mono<ResponseEntity<UniversalResponse>> getRoles(CommonWrapper commonWrapper){
        return userInterface.getAllRoles (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteRole(CommonRoleWrapper commonRoleWrapper){
        return userInterface.deleteRole (commonRoleWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

}
