package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Adapter {
    public String id;
    public Date createdAt;
    public Date updatedAt;
    public String channel;
    public String provider;
    public Config config;
    public String name;
}
