package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Transformer {
    public String id;
    public Date createdAt;
    public Date updatedAt;
    public Meta meta;
    public String transformerId;
    public String conversationLogicId;
}
