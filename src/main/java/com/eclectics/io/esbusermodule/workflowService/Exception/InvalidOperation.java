package com.eclectics.io.esbusermodule.workflowService.Exception;

/**
 * @author Alex Maina
 * @created 07/02/2023
 **/
public class InvalidOperation extends RuntimeException{
    public InvalidOperation(String message) {
        super (message);
    }
}
