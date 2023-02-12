package com.eclectics.io.esbusermodule.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Getter
@RequiredArgsConstructor
public enum SystemProcess {
    CREATE_ADMIN(true),
    UPDATE_ADMIN(true),
    ENABLE_ADMIN(true),
    DISABLE_ADMIN(true),
    DELETE_ADMIN(true),
    CREATE_PROFILE(true),
    DELETE_PROFILE(true),
    UPDATE_PROFILE(true),
    CREATE_ROLE(true),
    DELETE_ROLE(true),
    ASSIGN_ROLE_TO_PROFILE(true),
    REMOVE_ROLE_FROM_PROFILE(true),
    EDIT_MESSAGE_TEMPLATE(true),
    ADD_MESSAGE_TEMPLATE(true),
    CREATE_WORKFLOW(true),
    UPDATE_WORKFLOW(true),
    DELETE_WORKFLOW(true),
    ADD_WORKFLOW_STEP(true),
    UPDATE_WORKFLOW_STEP(true),
    DELETE_WORKFLOW_STEP(true);

    final boolean canCreateWorkFlow;

}
