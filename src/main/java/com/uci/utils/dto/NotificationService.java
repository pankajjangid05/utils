package com.uci.utils.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationService {
    private String serviceKey;
    private String username;
    private String password;
    private String senderId;
    private String secureKey;
    private String username2Way;
    private String password2Way;
    private String usernameHSM;
    private String passwordHSM;
}
