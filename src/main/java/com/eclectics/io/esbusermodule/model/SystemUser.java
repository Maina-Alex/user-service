package com.eclectics.io.esbusermodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
