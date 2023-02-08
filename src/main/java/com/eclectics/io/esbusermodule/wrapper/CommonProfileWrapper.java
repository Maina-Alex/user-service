package com.eclectics.io.esbusermodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author Alex Maina
 * @created 06/02/2023
 **/
@Getter
@Setter
@AllArgsConstructor
public class CommonProfileWrapper {
    @NotBlank(message = "Remarks cannot be null")
    private String remarks;
    @Min (value = 0, message = "Profile not found")
    private long id;
}
