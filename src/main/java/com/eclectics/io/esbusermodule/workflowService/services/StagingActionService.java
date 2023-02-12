package com.eclectics.io.esbusermodule.workflowService.services;



import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.workflowService.dto.ApproveStagedActionWrapper;
import com.eclectics.io.esbusermodule.workflowService.dto.StagingActionDto;
import com.eclectics.io.esbusermodule.workflowService.dto.WorkFlowDto;
import com.eclectics.io.esbusermodule.workflowService.model.StagingAction;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StagingActionService {
    UniversalResponse checkAndStageWorkFlow(StagingActionDto stagingActionDto);
    Mono<List<StagingActionDto>> getStagedActionsByGroupProcesses(StagingActionDto stagingActionDto);
    Mono<UniversalResponse> approveStagedActionByWorkFlowIdAndStagedActionId(ApproveStagedActionWrapper approveStagedActionWrapper);
    Mono<Void> resetAllStageApprovalsOnStepEdits(long workflowId);


}
