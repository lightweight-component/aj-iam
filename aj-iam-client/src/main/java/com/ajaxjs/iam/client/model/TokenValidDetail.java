package com.ajaxjs.iam.client.model;

import lombok.Data;

@Data
public class TokenValidDetail {
    private boolean isValid;

    private boolean isExpired;

    private long expiredTime;
}