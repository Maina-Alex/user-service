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
public class WorkSpaceDto {
    private String name;
    private String description;
    private String workSpaceId;
    private boolean isActive;
}
