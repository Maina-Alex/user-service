package com.eclectics.io.esbusermodule.service.impl;

import com.eclectics.io.esbusermodule.model.SystemUser;
import com.eclectics.io.esbusermodule.repository.ProfileRepository;
import com.eclectics.io.esbusermodule.repository.ProfileRoleRepository;
import com.eclectics.io.esbusermodule.repository.RoleRepository;
import com.eclectics.io.esbusermodule.repository.SystemUserRepository;
import com.eclectics.io.esbusermodule.serviceConfig.RedisStoreRepository;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Alex Maina
 * @created 09/02/2023
 **/
@RequiredArgsConstructor
@Service
public class RolesPublisher {
    private final ProfileRoleRepository profileRoleRepository;
    private final RedisStoreRepository redisStoreRepository;
    private final SystemUserRepository systemUserRepository;
    private final Gson gson;

    @Bean
    @SuppressWarnings({"UnstableApiUsage"})
    public Consumer<String> publishRoles(){
        return userAuthInfo-> {
            Map<String,Object> userMap= gson.fromJson (userAuthInfo, new TypeToken<Map<String,Object>>(){}.getType ());
            long userId= (long)userMap.get ("userId");
            long expiry= (long) userMap.get ("expiryTime");
            SystemUser systemUser= systemUserRepository.findTopByIdAndSoftDeleteFalse (userId)
                    .orElse (null);
            if(systemUser==null)
                return;
            List<String> userRoles= profileRoleRepository.findAllByProfileIdAndSoftDeleteFalse (systemUser.getProfile ().getId ())
                            .stream().map (profileRoles -> profileRoles.getRole ().getName ()).toList ();

            redisStoreRepository.saveAuthDetails (systemUser.getEmail (),userRoles,expiry)
                    .subscribeOn (Schedulers.boundedElastic ())
                    .subscribe ();
        };
    }
}
