package com.eclectics.io.esbusermodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserWrapper {
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 3, max = 15, message = "First Name should be between 3 and 15 characters")
    private String firstName;
    @Size(min=3,max=15, message = "Last Name should be between 3 and 15 characters")
    private String lastName;
    @Min (value = 1, message = "Select user profile")
    private long profileId;
}
