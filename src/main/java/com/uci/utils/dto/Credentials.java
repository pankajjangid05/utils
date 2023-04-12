package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Credentials {
    private String vault;
    private String variable;
}
