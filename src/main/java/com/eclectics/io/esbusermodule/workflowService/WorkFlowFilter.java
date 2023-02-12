package com.eclectics.io.esbusermodule.workflowService;

import com.eclectics.io.esbusermodule.constants.SystemProcess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkFlowFilter {
    SystemProcess processName();
}
