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
    private String id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private String description;
    private String adapterId;
    private ArrayList<Transformer> transformers;
    private Adapter adapter;
}
