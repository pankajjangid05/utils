package com.uci.utils.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class Config {
    private String dataObjName;
    private String phone;
    private Credentials credentials;
    private String url;
    private String type;
    private Cadence cadence;
    private String pageParam;
    private int totalRecords;
}
