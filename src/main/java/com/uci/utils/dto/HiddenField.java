package com.uci.utils.dto;

import lombok.*;

@ToString
@Getter
@Setter
public class HiddenField {
    private String name;
    private String path;
    private String type;
    private Config config;
}
