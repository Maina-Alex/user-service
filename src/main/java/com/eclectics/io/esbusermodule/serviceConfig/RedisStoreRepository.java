package com.eclectics.io.esbusermodule.serviceConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

/**
 * @author Alex Maina
 * @created 01/02/2023
 **/
@RequiredArgsConstructor
@Slf4j
@Repository
public class RedisStoreRepository {
    public static final String USERNAME_PREFIX = "esb_user:";
    private static final String USER_HASH="ESB_USER_HASH";
    private final Gson gson;
    private ReactiveHashOperations<String, String, String> hashOperations;
    private final ReactiveStringRedisTemplate redisOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisOperations.opsForHash ();
    }

    public Mono<Boolean> saveAuthDetails(String username, List<String> userRoles, long expiryTimeMinutes) {
        String userRolesString = gson.toJson (userRoles);
        return hashOperations.put (USERNAME_PREFIX+username, USER_HASH, userRolesString)
                .doOnError(throwable -> log.error("Failed to save user credentials===> {}",throwable.getLocalizedMessage()))
                .flatMap (bool-> redisOperations.expire (USERNAME_PREFIX+username, Duration.ofMinutes (expiryTimeMinutes)));
    }

    public Mono<List<String>> getUserRoles(String username) {
        return hashOperations.get (USERNAME_PREFIX+username,USER_HASH)
                .doOnError (err-> log.error ("Failed to get user roles error==> {} ",err.getLocalizedMessage ()))
                .map (res-> gson.fromJson (res, new TypeToken<List<String>> (){}.getType ()));
    }
}
