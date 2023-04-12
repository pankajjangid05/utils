package com.uci.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cadence {
    private int perPage;
    private int retries;
    private int timeout;
    private boolean concurrent;
    private boolean pagination;
    private int concurrency;
    @JsonProperty("retries-interval")
    private int retriesInterval;
}
