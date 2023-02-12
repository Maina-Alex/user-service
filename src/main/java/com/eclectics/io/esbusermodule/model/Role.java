package com.eclectics.io.esbusermodule.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "update tb_roles set soft_delete=true")
@Where(clause = "soft_delete = false")
public class Role extends BaseEntity {
    private String name;
    private String remarks;
    private boolean isSystemRole;
}
