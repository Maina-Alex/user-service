package com.eclectics.io.esbusermodule.workflowService.constants;

import lombok.Getter;

/**
 * @author Alex Maina
 * @created 03/08/2022
 **/
@Getter

public enum WorkFlowResponseStatus {
    STAGED(204),NOT_PRESENT(200);
    private final int status;
    WorkFlowResponseStatus (int status){
        this.status= status;
    }
}
