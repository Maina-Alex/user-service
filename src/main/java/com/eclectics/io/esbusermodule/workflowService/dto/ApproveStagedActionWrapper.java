package com.eclectics.io.esbusermodule.workflowService.dto;

import lombok.*;

/**
 * @author Alex Maina
 * @created 23/09/2022
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveStagedActionWrapper {
    private long stageId;
    private String workSpaceId;
    private boolean approved;
    private String approverDetails;
    private long approverId;
}
