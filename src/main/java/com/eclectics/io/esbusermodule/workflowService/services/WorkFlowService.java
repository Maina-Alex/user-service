package com.eclectics.io.esbusermodule.workflowService.services;


import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkFlowDto;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkFlowStepDto;
import reactor.core.publisher.Mono;

/**
 * @author Alex Maina
 * @created 23/09/2022
 **/
public interface WorkFlowService {
    Mono<UniversalResponse> createWorkFlow(WorkFlowDto workFlowDto);
    Mono<UniversalResponse> updateWorkFlow(WorkFlowDto workFlowDto);
    Mono<UniversalResponse> getWorkFlow(WorkFlowDto workFlowDto);
    Mono<UniversalResponse> deleteWorkFlow(WorkFlowDto workFlowDto);
    Mono<UniversalResponse> getStepsInWorkFlow(WorkFlowDto workFlowDto);
    Mono<UniversalResponse> addWorkFlowStep(WorkFlowStepDto workFlowStepDto);
    Mono<UniversalResponse> getWorkFlowStep(WorkFlowStepDto workFlowStepDto);

    Mono<UniversalResponse> updateWorkFlowStep(WorkFlowStepDto workFlowStepDto);
    Mono<UniversalResponse> removeWorkFlowStep(WorkFlowStepDto workFlowStepDto);
    Mono<UniversalResponse> reorderWorkFlowStep(WorkFlowDto workFlowDto);
}
