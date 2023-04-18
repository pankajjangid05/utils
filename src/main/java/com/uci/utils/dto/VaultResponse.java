package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class VaultResponse {
    private String apiId;
    private String path;
    private String apiVersion;
    private String msgid;
    private Map<String, NotificationService> result;
    private String startTime;
    private String method;
    private String ownerId;
    private String endTime;
}
