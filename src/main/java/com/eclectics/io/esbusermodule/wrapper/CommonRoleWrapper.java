package com.eclectics.io.esbusermodule.wrapper;

import com.eclectics.io.esbusermodule.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Alex Maina
 * @created 06/02/2023
 **/
@Getter
@Setter
@Builder
public class CommonRoleWrapper {
    private long roleId;
    private String name;
    private  String remarks;
    private boolean systemRole;
    private long profileId;
    private List<Long> roleList;
}
