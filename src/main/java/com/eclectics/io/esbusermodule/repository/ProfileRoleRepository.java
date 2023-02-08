package com.eclectics.io.esbusermodule.repository;

import com.eclectics.io.esbusermodule.model.ProfileRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRoleRepository extends JpaRepository<ProfileRoles,Long> {
    List<ProfileRoles> findAllByProfileIdAndSoftDeleteFalse(long profileId);

    boolean existByProfileIdAndRoleIdAndSoftDeleteFalse(long profileId, long roleId);

    Optional<ProfileRoles> findByProfileIdAndRoleIdAndSoftDeleteFalse(long profileId, long roleId);
}
