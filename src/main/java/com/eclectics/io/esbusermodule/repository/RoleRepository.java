package com.eclectics.io.esbusermodule.repository;

import com.eclectics.io.esbusermodule.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findAllBySoftDeleteFalse();
    Optional<Role> findByNameAndSoftDeleteFalse(String name);

    Optional<Role> findByIdAndSoftDeleteFalse(long roleId);
}
