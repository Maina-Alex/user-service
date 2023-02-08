package com.eclectics.io.esbusermodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alex Maina
 * @created 06/02/2023
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonWrapper {
    private long id;
    private String remarks;
    private int page;
    private int size;
    private String filter;
}
