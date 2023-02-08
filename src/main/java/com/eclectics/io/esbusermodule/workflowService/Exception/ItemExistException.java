package com.eclectics.io.esbusermodule.workflowService.Exception;

/**
 * @author Alex Maina
 * @created 07/02/2023
 **/
public class ItemExistException extends RuntimeException{
    public ItemExistException(String message) {
        super (message);
    }
}
