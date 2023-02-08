package com.eclectics.io.esbusermodule.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Getter
@RequiredArgsConstructor
public enum SystemProcesses {
    CREATE_ADMIN(true,"USM-001"),
    UPDATE_ADMIN(true,"USM-002"),
    ENABLE_ADMIN(true,"USM-003"),
    DISABLE_ADMIN(true,"USM-004"),
    DELETE_ADMIN(true,"USM-005"),
    CREATE_PROFILE(true,"USM-006"),
    DELETE_PROFILE(true,"USM-007"),
    DISABLE_PROFILE(true,"USM-008"),
    CREATE_ROLE(true,"USM-009"),
    DISABLE_ROLE(true,"USM-010"),
    DELETE_ROLE(true,"USM-011"),
    EDIT_MESSAGE_TEMPLATE(true,"USM-012"),
    ADD_MESSAGE_TEMPLATE(true,"USM-013"),
    CREATE_WORKFLOW(true,"USM-014"),
    UPDATE_WORKFLOW(true,"USM-015"),
    DELETE_WORKFLOW(true,"USM-016");

    final boolean canCreateWorkFlow;
    final String processIdentifier;

}
