package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.IBaseModel;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.sqlman.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table("user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel implements UserConstants, IBaseModel {
    /**
     * 部门 ID
     */
    private Long orgId;

    /**
     * 租户 id
     */
    private Long tenantId;

    private String tenantName;

    /**
     * 登录名、用户登录 id，不可重复
     */
    @NotNull
    private String loginId;

    /**
     * 用户名称/昵称
     */
    private String username;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 数据字典：性别
     */
    private Gender gender;
//    private String gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 地区
     */
    private String location;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 头像（二进制文件，base64 编码）
     */
    private String avatarBlob;

    /**
     * 邮件
     */
    @Email
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 是否验证了（手机号码、邮箱、实名、银行卡）的状态总值，采用 8421 码
     */
    private Integer verifiedState;

    /**
     * 绑定第三方登录账号的状态总值，采用 8421 码
     */
    private Integer bindState;

    /**
     * 身份证号码
     */
    private String idCardNo;

    private String jobTitle;

    private String address;

//    private Long locationProvince;
//
//    private Long locationCity;
//
//    private Long locationDistrict;

}
