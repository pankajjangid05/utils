package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@Getter
@Setter
public class Meta {
    public String body;
    public String form;
    public String title;
    public String formID;
    public ArrayList<HiddenField> hiddenFields;
    public String serviceClass;
    public String templateType;
    public Data data;
    public String type;
}
