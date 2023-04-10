package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cadence {
    public int perPage;
    public int retries;
    public int timeout;
    public boolean concurrent;
    public boolean pagination;
    public int concurrency;
    public int retriesInterval;
}
