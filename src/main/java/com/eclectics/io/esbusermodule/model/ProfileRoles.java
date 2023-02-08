package com.eclectics.io.esbusermodule.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Alex Maina
 * @created 25/01/2023
 **/
@Table(name = "tb_profile_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProfileRoles extends BaseEntity {
    @ManyToOne
    private Profile profile;
    @ManyToOne
    private Role role;

}
