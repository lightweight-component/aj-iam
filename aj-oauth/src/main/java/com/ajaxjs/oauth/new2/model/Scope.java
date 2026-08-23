package com.ajaxjs.oauth.new2.model;

import com.ajaxjs.sqlman.annotation.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table("oauth_scope")
public class Scope {
    private Long id;

    private String scope;

    private String description;

    private Boolean isDefault;

    /**
     * 额外说明
     */
    private Boolean memo;

    private String creator;

    private Date createDate;
}
