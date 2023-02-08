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
@Table(name = "tb_corporate_staging_workflows")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class WorkFlow extends BaseEntity {
    private String name;
    private String remarks;
    private String process;
    private String workflowStepsOrder;
}
