package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Adapter {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String channel;
    private String provider;
    private Config config;
    private String name;
}
