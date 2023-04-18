package com.uci.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Setter
@Getter
@ToString
public class Result {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String startingMessage;
    private String ownerID;
    private String ownerOrgID;
    private String purpose;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private ArrayList<String> tags;
    private ArrayList<Users> users;
    private ArrayList<LogicID> logicIDs;
    private String userId;
    private Config config;
    private String provider;
    private String channel;
}
