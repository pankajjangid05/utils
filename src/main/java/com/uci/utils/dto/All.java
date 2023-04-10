package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class All {
    public String id;
    public Date createdAt;
    public Date updatedAt;
    public String type;
    public Config config;
    public Object name;
}