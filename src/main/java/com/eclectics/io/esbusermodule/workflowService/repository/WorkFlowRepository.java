package com.eclectics.io.esbusermodule.workflowService.repository;

import com.eclectics.io.esbusermodule.workflowService.model.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long> {
    Optional<WorkFlow> findByProcessAndSoftDeleteFalse(String process);

    Optional<WorkFlow> findByIdOrProcessEqualsIgnoreCaseAndSoftDeleteFalse(long workFlowId, String process);

    Optional<WorkFlow> findByIdAndSoftDeleteFalse(long workFlowId);

}
