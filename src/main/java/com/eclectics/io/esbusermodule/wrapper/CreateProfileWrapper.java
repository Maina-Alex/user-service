package com.eclectics.io.esbusermodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.Size;

/**
 * @author Alex Maina
 * @created 06/02/2023
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileWrapper {
    @Size(min = 4, max = 10, message = "Profile name must be within 4-10 characters long")
    private String name;
    @Size(message = "Profile Description must be within 4-100 characters long")
    private String remarks;
}
