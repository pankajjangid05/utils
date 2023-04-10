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
    public String id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public String startingMessage;
    public String ownerID;
    public String ownerOrgID;
    public String purpose;
    public String description;
    public String startDate;
    public String endDate;
    public String status;
    public ArrayList<String> tags;
    public ArrayList<Users> users;
    public ArrayList<LogicID> logicIDs;
    @JsonProperty("nl-app-firebase-notification")
    public NlAppFirebaseNotification nlAppFirebaseNotification;

}
