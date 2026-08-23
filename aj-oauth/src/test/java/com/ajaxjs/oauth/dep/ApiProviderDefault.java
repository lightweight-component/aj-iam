package com.ajaxjs.oauth.dep;

import com.ajaxjs.oauth.ApiProvider;

public enum ApiProviderDefault implements ApiProvider {
    /**
     * 新版企业微信 Web 登录（扫码），参考 <a href="https://developer.work.weixin.qq.com/document/path/98152">https://developer.work.weixin.qq.com/document/path/98152</a>
     */
    WECHAT_ENTERPRISE_V2 {
        @Override
        public String authorize() {
            return "https://login.work.weixin.qq.com/wwlogin/sso/login";
        }

        @Override
        public String accessToken() {
            return "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        }

        @Override
        public String userInfo() {
            return "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo";
        }

    },
    /**
     * 企业微信二维码第三方登录
     */
    WECHAT_ENTERPRISE_QRCODE_THIRD {
        /**
         * 授权的api
         *
         * @return url
         */
        @Override
        public String authorize() {
            return "https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect";
        }

        /**
         * 获取accessToken的api
         *
         * @return url
         */
        @Override
        public String accessToken() {
            return "https://qyapi.weixin.qq.com/cgi-bin/service/get_provider_token";
        }

        /**
         * 获取用户信息的api
         *
         * @return url
         */
        @Override
        public String userInfo() {
            return "https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info";
        }


    },
    /**
     * 企业微信网页登录
     */
    WECHAT_ENTERPRISE_WEB {
        @Override
        public String authorize() {
            return "https://open.weixin.qq.com/connect/oauth2/authorize";
        }

        @Override
        public String accessToken() {
            return "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        }

        @Override
        public String userInfo() {
            return "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
        }


    },


    /**
     * 微信小程序授权登录
     *
     * @since yudaocode
     */
    WECHAT_MINI_PROGRAM {
        @Override
        public String authorize() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html 文档
            throw new UnsupportedOperationException("不支持获取授权 url，请使用小程序内置函数 wx.login() 登录获取 code");
        }

        @Override
        public String accessToken() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html 文档
            // 获取 openid, unionId , session_key 等字段
            return "https://api.weixin.qq.com/sns/jscode2session";
        }

        @Override
        public String userInfo() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/api/open-api/user-info/wx.getUserProfile.html 文档
            throw new UnsupportedOperationException("不支持获取用户信息 url，请使用小程序内置函数 wx.getUserProfile() 获取用户信息");
        }


    },

    /**
     * QQ小程序授权登录
     */
    QQ_MINI_PROGRAM {
        @Override
        public String authorize() {
            // 参见 https://q.qq.com/wiki/develop/miniprogram/frame/open_ability/open_userinfo.html 文档
            throw new UnsupportedOperationException("不支持获取授权 url，请使用小程序内置函数 qq.login() 登录获取 code");
        }

        @Override
        public String accessToken() {
            // 参见 https://q.qq.com/wiki/develop/miniprogram/server/open_port/port_login.html 文档
            // 获取 openid, unionId , session_key 等字段
            return "https://api.q.qq.com/sns/jscode2session";
        }

        @Override
        public String userInfo() {
            // 参见 https://q.qq.com/wiki/develop/miniprogram/API/open_port/port_userinfo.html 文档
            throw new UnsupportedOperationException("不支持获取用户信息 url，请使用小程序内置函数 qq.getUserInfo() 获取用户信息");
        }
    },

    /**
     * 酷家乐
     *
     * @since 1.11.0
     */
    KUJIALE {
        @Override
        public String authorize() {
            return "https://oauth.kujiale.com/oauth2/show";
        }

        @Override
        public String accessToken() {
            return "https://oauth.kujiale.com/oauth2/auth/token";
        }

        @Override
        public String userInfo() {
            return "https://oauth.kujiale.com/oauth2/openapi/user";
        }

        @Override
        public String refresh() {
            return "https://oauth.kujiale.com/oauth2/auth/token/refresh";
        }


    },

    /**
     * Gitlab
     *
     * @since 1.11.0
     */
    GITLAB {
        @Override
        public String authorize() {
            return "https://gitlab.com/oauth/authorize";
        }

        @Override
        public String accessToken() {
            return "https://gitlab.com/oauth/token";
        }

        @Override
        public String userInfo() {
            return "https://gitlab.com/api/v4/user";
        }


    },


    /**
     * Okta，
     * <p>
     * 团队/组织的域名不同，此处通过配置动态组装
     *
     * @since 1.16.0
     */
    OKTA {
        @Override
        public String authorize() {
            return "https://%s.okta.com/oauth2/%s/v1/authorize";
        }

        @Override
        public String accessToken() {
            return "https://%s.okta.com/oauth2/%s/v1/token";
        }

        @Override
        public String refresh() {
            return "https://%s.okta.com/oauth2/%s/v1/token";
        }

        @Override
        public String userInfo() {
            return "https://%s.okta.com/oauth2/%s/v1/userinfo";
        }

        @Override
        public String revoke() {
            return "https://%s.okta.com/oauth2/%s/v1/revoke";
        }
    },
    /**
     * 程序员客栈
     *
     * @since 1.16.2
     */
    PROGINN {
        @Override
        public String authorize() {
            return "https://www.proginn.com/oauth2/authorize";
        }

        @Override
        public String accessToken() {
            return "https://www.proginn.com/oauth2/access_token";
        }

        @Override
        public String userInfo() {
            return "https://www.proginn.com/openapi/user/basic_info";
        }


    },

    /**
     * Teambition
     */
    TEAMBITION {
        @Override
        public String authorize() {
            return "https://account.teambition.com/oauth2/authorize";
        }

        @Override
        public String accessToken() {
            return "https://account.teambition.com/oauth2/access_token";
        }

        @Override
        public String refresh() {
            return "https://account.teambition.com/oauth2/refresh_token";
        }

        @Override
        public String userInfo() {
            return "https://api.teambition.com/users/me";
        }


    },

    FIGMA {
        @Override
        public String authorize() {
            return "https://www.figma.com/oauth";
        }

        @Override
        public String accessToken() {
            return "https://www.figma.com/api/oauth/token";
        }

        @Override
        public String userInfo() {
            return "https://api.figma.com/v1/me";
        }

        @Override
        public String refresh() {
            return "https://www.figma.com/api/oauth/refresh";
        }

    },


    /**
     * Coding，
     * <p>
     * 参考 <a href="https://help.coding.net/docs/project/open/oauth.html#%E7%94%A8%E6%88%B7%E6%8E%88%E6%9D%83">...</a> 中的说明，
     * 新版的 coding API 地址需要传入用户团队名，这儿使用动态参数，方便在 request 中使用
     */
    CODING {
        @Override
        public String authorize() {
            return "https://%s.coding.net/oauth_authorize.html";
        }

        @Override
        public String accessToken() {
            return "https://%s.coding.net/api/oauth/access_token";
        }

        @Override
        public String userInfo() {
            return "https://%s.coding.net/api/account/current_user";
        }


    },

}
