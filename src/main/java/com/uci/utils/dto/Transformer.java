package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Transformer {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private Meta meta;
    private String transformerId;
    private String conversationLogicId;
}
