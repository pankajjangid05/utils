package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
public class BotServiceResponse {
    public String apiId;
    public String path;
    public String apiVersion;
    public String msgid;
    public ArrayList<Result> result;
    public Date startTime;
    public String method;
    public String ownerId;
    public Date endTime;
}
