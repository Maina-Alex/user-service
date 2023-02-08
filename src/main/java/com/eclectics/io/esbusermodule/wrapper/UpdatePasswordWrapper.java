package com.eclectics.io.esbusermodule.wrapper;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author Alex Maina
 * @created 04/02/2023
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordWrapper {
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
    @Min (value=1)
    private long id;
}
