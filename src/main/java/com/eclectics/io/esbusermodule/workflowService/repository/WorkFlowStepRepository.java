package com.eclectics.io.esbusermodule.workflowService.repository;

import com.eclectics.io.esbusermodule.workflowService.model.WorkFlowStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkFlowStepRepository extends JpaRepository<WorkFlowStep,Long> {
    List<WorkFlowStep> findAllByWorkFlowIdAndSoftDeleteFalse(long workFlowId);

    Optional<WorkFlowStep> findByIdAndWorkFlowIdEqualsAndSoftDeleteFalse(long workflowStepId, long workFlowId);
    Optional<WorkFlowStep> findByStepNameEqualsIgnoreCaseAndWorkFlowIdAndSoftDeleteFalse(String name, long workFlowId);

    Optional<WorkFlowStep> findByIdAndWorkFlowIdAndSoftDeleteFalse(long workFlowStepId, long workFlowId);
}
