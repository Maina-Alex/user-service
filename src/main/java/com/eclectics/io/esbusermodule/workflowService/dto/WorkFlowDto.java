package com.eclectics.io.esbusermodule.workflowService.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alex Maina
 * @created 23/09/2022
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkFlowDto {
    private long id;
    private String name;
    private String remarks;
    private String process;
    private String workSpaceId;
    private LocalDate createdDate;
    private LocalDate updateTime;
    private boolean active=true;
    private List<Long> workFlowStepsOrder;
}

