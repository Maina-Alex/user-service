package com.eclectics.io.esbusermodule.workflowService.services.impl;

import com.eclectics.io.esbusermodule.service.impl.UserService;
import com.eclectics.io.esbusermodule.util.UniversalResponse;
import com.eclectics.io.esbusermodule.workflowService.WorkFlowFilter;
import com.eclectics.io.esbusermodule.workflowService.constants.WorkFlowResponseStatus;
import com.eclectics.io.esbusermodule.workflowService.dto.StagingActionDto;
import com.eclectics.io.esbusermodule.workflowService.services.StagingActionService;
import com.eclectics.io.esbusermodule.wrapper.WorkFlowUserWrapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Alex Maina
 * @created 12/02/2023
 **/
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowAspectHelper {
    private final UserService userService;
    private final StagingActionService stagingActionService;
    private final Gson gson;

    @Around("@annotation(com.eclectics.io.esbusermodule.workflowService.WorkFlowFilter)")
    public Mono<Object> checkAndStageWorkFlow(ProceedingJoinPoint point) {
        MethodSignature method = (MethodSignature) point.getSignature ();
        Object[] obj = point.getArgs ();
        String request = gson.toJson (obj[0]);
        WorkFlowFilter workFlowFilter = method.getMethod ().getAnnotation (WorkFlowFilter.class);
        String process = workFlowFilter.processName ().name ();
        return ReactiveSecurityContextHolder.getContext ()
                .flatMap (ctx-> Mono.just (ctx.getAuthentication ()))
                .flatMap (auth -> userService.getSystemUserByUsername (auth.getName ()))
                .map (user -> {
                    StagingActionDto stagingActionDto = StagingActionDto.builder ()
                            .stagingUserDetails (gson.toJson (new WorkFlowUserWrapper (user)))
                            .stagingCurrentData (gson.toJson (request))
                            .stagingPreviousData (null)
                            .process (process)
                            .build ();
                    WorkFlowResponseStatus workFlowResponseStatus = stagingActionService.checkAndStageWorkFlow (stagingActionDto);
                    if (workFlowResponseStatus == WorkFlowResponseStatus.STAGED) {
                        return UniversalResponse.builder ().status (200).message ("Request staged for approval").build ();
                    }
                    try {
                        return point.proceed ();
                    } catch (Throwable e) {
                        throw new RuntimeException (e);
                    }
                }).doOnError (error -> {
                    log.error ("Workflow Error ===> {}", error.getLocalizedMessage ());
                    throw new RuntimeException (error);
                }).publishOn (Schedulers.boundedElastic ());
    }
}
