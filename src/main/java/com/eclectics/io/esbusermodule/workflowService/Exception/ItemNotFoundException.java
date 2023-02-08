package com.eclectics.io.esbusermodule.workflowService.Exception;

/**
 * @author Alex Maina
 * @created 07/02/2023
 **/
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super (message);
    }
}
