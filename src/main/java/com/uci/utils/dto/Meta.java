package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;

@ToString
@Getter
@Setter
public class Meta {
    private String body;
    private String form;
    private String title;
    private String formID;
    private ArrayList<HiddenField> hiddenFields;
    private String serviceClass;
    private String templateType;
    private HashMap<String, String> data;
    private String type;
    private ArrayList<String> params;
    private String templateId;
}
