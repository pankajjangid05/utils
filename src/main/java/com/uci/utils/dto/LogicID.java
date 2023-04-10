package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Setter
@Getter
@ToString
public class LogicID {
    public String id;
    public String name;
    public Date createdAt;
    public Date updatedAt;
    public String description;
    public String adapterId;
    public ArrayList<Transformer> transformers;
    public Adapter adapter;
}
