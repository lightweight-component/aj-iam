package com.ajaxjs.oauth.impl.stackoverflow;

import com.ajaxjs.oauth.model.Config;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class StackOverflowConfig extends Config {
    private String stackOverflowKey;
}
