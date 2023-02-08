package com.eclectics.io.esbusermodule.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alex Maina
 * @created 04/08/2022
 **/
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Tuple<T,V>{
    T t;
    V v;
}
