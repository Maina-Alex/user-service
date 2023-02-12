package com.eclectics.io.esbusermodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Table(name="tb_system_users",indexes = @Index (columnList = "email,username"))
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "update tb_system_users set soft_delete=true")
@Where(clause = "soft_delete = false")
public class SystemUser extends BaseEntity{
    @JsonIgnore
    private String userName;
    @JsonIgnore
    private String password;
    private String email;
    private boolean firstTimeLogin= Boolean.TRUE;
    private String firstName;
    private String lastName;
    private String remarks;
    private boolean blocked=false;
    private String blockedBy;
    private String blockedRemarks;
    private int loginAttempts;
    private int resetPasswordLoginCount;
    private Date passwordResetAt;
    @ManyToOne
    private Profile profile;
}
