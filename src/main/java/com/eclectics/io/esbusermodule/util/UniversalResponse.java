package com.eclectics.io.esbusermodule.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * @author Alex Maina
 * @created 16/05/2022
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class UniversalResponse {
    private Integer status;
    private String message;
    private Object data;
    private List<String> errors;
    private Integer totalItems;
}
