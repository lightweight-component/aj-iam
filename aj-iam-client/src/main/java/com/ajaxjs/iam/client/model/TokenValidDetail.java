package com.ajaxjs.iam.client.model;

import lombok.Data;

@Data
public class TokenValidDetail {
    boolean isValid;

    boolean isExpired;

    long expiredTime;
}
