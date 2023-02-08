package com.eclectics.io.esbusermodule.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Alex Maina
 * @created 25/01/2023
 **/
@Getter
@Setter
@Entity
@Table(name = "tb_roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    private String name;
    private String remarks;
    private boolean isSystemRole;
}
