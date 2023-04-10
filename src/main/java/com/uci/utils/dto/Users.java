package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class Users {
    public String id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public Object description;
    public int count;
    public Object category;
    public String allServiceID;
    public String byPhoneServiceID;
    public Object byIDServiceID;
    public Object botId;
    public All all;
}