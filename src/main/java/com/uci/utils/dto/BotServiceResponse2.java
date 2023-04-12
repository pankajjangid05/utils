package com.uci.utils.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BotServiceResponse2 {
    private String apiId;
    private String path;
    private String apiVersion;
    private String msgid;
    private Result result;
    private Date startTime;
    private String method;
    private String ownerId;
    private Date endTime;
}
