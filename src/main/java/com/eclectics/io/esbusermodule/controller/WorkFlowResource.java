package com.eclectics.io.esbusermodule.controller;


import com.eclectics.io.esbusermodule.service.impl.UserService;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.workflowService.dto.ApproveStagedActionWrapper;
import com.eclectics.io.esbusermodule.workflowService.dto.StagingActionDto;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkFlowDto;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkFlowStepDto;
import com.eclectics.io.esbusermodule.workflowService.services.StagingActionService;
import com.eclectics.io.esbusermodule.workflowService.services.WorkFlowService;
import com.eclectics.io.esbusermodule.wrapper.CommonWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Alex Maina
 * @created 19/07/2022
 **/
@RequestMapping("/api/v1/corporate/workflow")
@RequiredArgsConstructor
@RestController
public class WorkFlowResource {
    private final WorkFlowService workFlowService;
    private final StagingActionService stagingActionService;
    private final UserService userService;

    @PostMapping("/create")
    public Mono<ResponseEntity<UniversalResponse>> createWorkFlow(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.createWorkFlow (workFlowDto).map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get/id")
    public Mono<ResponseEntity<UniversalResponse>> getWorkFlowById(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.getWorkFlow (workFlowDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get/workflows")
    public Mono<ResponseEntity<UniversalResponse>> getWorkFlows(CommonWrapper commonWrapper) {
        return workFlowService.getWorkFlows (commonWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/update")
    public Mono<ResponseEntity<UniversalResponse>> updateWorkFlow(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.updateWorkFlow (workFlowDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteWorkFlow(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.deleteWorkFlow (workFlowDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());

    }

    @PostMapping("/create/step")
    public Mono<ResponseEntity<UniversalResponse>> createWorkFlowStep(@RequestBody WorkFlowStepDto workFlowStepDto) {
        return workFlowService.addWorkFlowStep (workFlowStepDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/order/steps")
    public Mono<ResponseEntity<UniversalResponse>> orderWorkFlowSteps(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.reorderWorkFlowStep (workFlowDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get/step")
    public Mono<ResponseEntity<UniversalResponse>> getWorkFlowStepById(@RequestBody WorkFlowStepDto workFlowStepDto) {
        return workFlowService.getWorkFlowStep (workFlowStepDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/get/workflow/steps/")
    public Mono<ResponseEntity<UniversalResponse>> getWorkFlowStepsByWorkFlowId(@RequestBody WorkFlowDto workFlowDto) {
        return workFlowService.getStepsInWorkFlow (workFlowDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/update/step")
    public Mono<ResponseEntity<UniversalResponse>> updateWorkFlowSteps(@RequestBody WorkFlowStepDto workFlowStepDto) {
        return workFlowService.updateWorkFlowStep (workFlowStepDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());

    }

    @PostMapping("/delete/step")
    public Mono<ResponseEntity<UniversalResponse>> deleteWorkFlowStep(@RequestBody WorkFlowStepDto workFlowStepDto) {
        return workFlowService.removeWorkFlowStep (workFlowStepDto)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/approve/staged")
    public Mono<ResponseEntity<UniversalResponse>> approveWorkflow(@RequestBody ApproveStagedActionWrapper approveStagedActionWrapper) {
        return stagingActionService.approveStagedActionByWorkFlowIdAndStagedActionId (approveStagedActionWrapper)
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }

    @PostMapping("/staged")
    public Mono<ResponseEntity<UniversalResponse>> getStagedWorkflows(@RequestBody StagingActionDto stagingActionDto, Authentication authentication) {
        return userService.getUserProfile (authentication.getName ())
                .flatMap (profile -> {
                    stagingActionDto.setApproverId (profile.getId ());
                    return Mono.just (stagingActionDto);
                })
                .flatMap (stagingActionService::getStagedActionsByGroupProcesses)
                .flatMap (response -> Mono.just (UniversalResponse.builder ().data (response).message ("Staged workflows").status (200).build ()))
                .map (ResponseEntity::ok)
                .publishOn (Schedulers.boundedElastic ());
    }


}
