package com.eclectics.io.esbusermodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * @author Alex Maina
 * @created 02/02/2023
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserWrapper {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    private long id;
    @Nullable
    private Long profileId;
}
