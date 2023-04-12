package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class Users {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private Object description;
    private int count;
    private Object category;
    private String allServiceID;
    private String byPhoneServiceID;
    private Object byIDServiceID;
    private Object botId;
    private All all;
}