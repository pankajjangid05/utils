package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Config {
    public String dataObjName;
    public String phone;
    public Credentials credentials;
    public String url;
    public String type;
    public Cadence cadence;
    public String pageParam;
    public int totalRecords;
}
