package com.eclectics.io.esbusermodule.wrapper;

import lombok.*;

import javax.validation.constraints.Min;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Data
public class ResetUserPassword {
    @Min (value = 1, message = "User not found")
    private long userId;

}
