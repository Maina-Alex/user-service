package com.eclectics.io.esbusermodule.workflowService.dto;

import lombok.*;

/**
 * @author Alex Maina
 * @created 24/09/2022
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkFlowStepDto {
    private long id;
    private String stepName;
    private String remarks;
    private Long requiredRoleId;
    private Long workFlowId;
    private String workSpaceId;
    private String notificationEmail;
    private String notificationEmailMessage;
}
