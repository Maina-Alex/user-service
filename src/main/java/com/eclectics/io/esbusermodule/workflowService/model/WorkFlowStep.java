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
@Table(name = "tb_corporate_staging_workflows_steps")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class WorkFlowStep extends BaseEntity {
    private String stepName;
    private String remarks;
    private Long   requiredRoleId;
    private String notificationEmail;
    private String notificationEmailMessage;
    @ManyToOne
    private WorkFlow workFlow;
}
