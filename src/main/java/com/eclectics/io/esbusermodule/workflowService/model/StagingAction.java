package com.eclectics.io.esbusermodule.workflowService.model;

import com.eclectics.io.esbusermodule.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
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
@Table(name = "tb_corporate_workflow_staging_action")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class StagingAction  extends BaseEntity {
    @Lob
    private String stagingUserDetails;
    @Lob
    private String stagingCurrentData;
    @Lob
    private String stagingPreviousData;
    private String metaData;
    private boolean finalized= false;
    private boolean approved=false;
    private boolean processed=false;
    private int currentStepIndex=0;
    private String process;
    @ManyToOne
    private WorkFlow workflow;

}
