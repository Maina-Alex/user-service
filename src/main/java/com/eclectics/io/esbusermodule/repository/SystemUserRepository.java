package com.eclectics.io.esbusermodule.repository;

import com.eclectics.io.esbusermodule.model.SystemUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alex Maina
 * @created 25/01/2023
 **/
public interface SystemUserRepository extends JpaRepository<SystemUser,Long> {
    Optional<SystemUser> findTopByEmailAndSoftDeleteFalse(String email);
    Optional<SystemUser> findTopByIdAndSoftDeleteFalse(long userId);

    Page<SystemUser> findAllBySoftDeleteFalse(Pageable pageable);
    Page<SystemUser> findAllByBlockedFalseAndSoftDeleteFalse(Pageable pageable);

    Page<SystemUser> findAllByBlockedTrueAndSoftDeleteFalse(Pageable pageable);

    boolean existsByProfileIdAndSoftDeleteFalse(long profileId);

}
