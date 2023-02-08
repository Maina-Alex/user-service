package com.eclectics.io.esbusermodule.workflowService.model;

import com.eclectics.io.esbusermodule.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Alex Maina
 * @created 23/09/2022
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_corporate_workflow_staging_action_approval")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class StagingActionApproval extends BaseEntity {
    private String approvalAction;
    private String  checkerDetails;
    private String remarks;
    @ManyToOne
    private StagingAction stagingAction;
}
