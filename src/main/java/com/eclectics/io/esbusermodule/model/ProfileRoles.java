package com.eclectics.io.esbusermodule.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "update tb_profile_roles set soft_delete=true")
@Where(clause = "soft_delete = false")
public class ProfileRoles extends BaseEntity {
    @ManyToOne
    private Profile profile;
    @ManyToOne
    private Role role;
}
