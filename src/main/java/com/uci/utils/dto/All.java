package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class All {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String type;
    private Config config;
    private Object name;
}