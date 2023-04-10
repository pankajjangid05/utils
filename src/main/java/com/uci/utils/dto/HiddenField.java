package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class HiddenField {
    public String name;
    public String path;
    public String type;
    public Config config;
}
