package com.eclectics.io.esbusermodule.workflowService.services;



import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkSpaceDto;
import reactor.core.publisher.Mono;

public interface WorkSpaceService {
    Mono<UniversalResponse> getWorkSpaceByName(WorkSpaceDto workSpaceDto);
    Mono<UniversalResponse> createWorkSpace(WorkSpaceDto workSpaceDto);
    Mono<UniversalResponse> getWorkFlowsInWorkSpace(WorkSpaceDto workSpaceDto);
}
