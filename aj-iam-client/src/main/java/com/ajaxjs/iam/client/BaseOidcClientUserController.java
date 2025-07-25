package com.ajaxjs.iam.client;

import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.http_request.Post;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public abstract class BaseOidcClientUserController {
    @Value("${auth.iam_service: }")
    private String iamService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    static Map<String, Object> TEMP_CACHE = new HashMap<>();

    /**
     * 获取重定向视图，用于认证流程中的第一步：跳转到登录页面。
     *
     * @param session           HttpSession 对象，用于保存和管理用户会话信息。
     * @param userLoginCode     用户登录代码，通常是登录页面的 URL 后缀。
     * @param clientId          客户端 ID，用于识别请求 OAuth 服务的应用。
     * @param clientCallbackUrl 应用的网站 URL，授权服务器完成授权后会重定向到该 URL 的回调接口。
     * @param webUrl            前端页面地址，用于跳到这里以便获取 Token。
     * @return RedirectView 返回一个重定向视图对象，包含了构造的重定向 URL。
     */
    public RedirectView loginPageUrl(HttpSession session, String userLoginCode, String clientId, String clientCallbackUrl, String webUrl) {
        String state = RandomTools.generateRandomString(5);
//        session.setAttribute(ClientUtils.OAUTH_STATE, state);// 将 state 值保存到会话中
        TEMP_CACHE.put(ClientUtils.OAUTH_STATE, state);

        log.info("set state code:" + state);

        String url = userLoginCode + "?response_type=code&client_id=" + clientId;
        url += "&redirect_uri=" + urlEncode(clientCallbackUrl);
        url += "&state=" + state;

        if (StringUtils.hasText(webUrl))
            url += "&web_url=" + urlEncode(webUrl);

        return new RedirectView(url);
    }

    /**
     * UTF-8 字符串而已
     */
    public static final String UTF8_SYMBOL = "UTF-8";

    /**
     * URL 编码
     *
     * @param str 输入的字符串
     * @return URL 编码后的字符串
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, UTF8_SYMBOL);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 用 AccessToken 可用的时候
     */
    public abstract JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpServletResponse resp, HttpSession session);

    public ModelAndView callbackToken(String clientId, String clientSecret, String code, String state, String webUrl, HttpSession session, HttpServletResponse resp) {
        // 从会话中获取之前保存的 state 值
//        String savedState = (String) session.getAttribute(ClientUtils.OAUTH_STATE);
        String savedState = (String) TEMP_CACHE.get(ClientUtils.OAUTH_STATE);

        if (!state.equals(savedState)) { // 检查返回的 state 值是否与之前保存的值匹配
            ClientUtils.returnForbidden(resp);
            log.warn("state code error, in session: " + savedState);

            return null;
        } else
            session.removeAttribute(ClientUtils.OAUTH_STATE);

        log.info("code:" + code);
        log.info("state:" + state);
        log.info("clientId:" + clientId);
        log.info("clientSecret:" + clientSecret);
        String tokenApi = getIamService() + "/oidc/token";

        Map<String, String> params = ObjectHelper.mapOf("grant_type", "authorization_code", "code", code, "state", state);
        Map<String, Object> result = Post.api(tokenApi, params, conn -> conn.setRequestProperty("Authorization", ClientCredentials.encodeClient(clientId, clientSecret)));

        if (result != null) {// 处理授权成功的逻辑，例如解析并保存访问令牌和刷新令牌等
            if ((int) result.get("status") == 1) {
                JwtAccessToken jwt = JsonUtil.map2pojo((Map<String, Object>) result.get("data"), JwtAccessToken.class);
                onAccessTokenGot(jwt, resp, session);

                if (StringUtils.hasText(webUrl)) {
//                    return new ModelAndView(new RedirectView(webUrl + "?token=" + urlEncode(jwt.getId_token())));
                    return new ModelAndView(new RedirectView(webUrl));// 不要这样发送 token，改 cookie
                } else
                    return new ModelAndView("redirect:/");
            } else {
                log.info("error:" + result);
                throw new SecurityException("获取 JWT Token 失败，原因: " + result.get("message"));
            }
        } else {

//			 处理授权失败的逻辑
            throw new SecurityException("获取 JWT Token 失败");
        }
    }

    public JwtAccessToken ropcLogin(String username, String password) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("grant_type", "password");
        bodyParams.put("username", username);
        bodyParams.put("password", password);
        bodyParams.put("client_id", clientId);
        bodyParams.put("client_secret", clientSecret);
        String tokenApi = getIamService() + "/oidc/token";
        Map<String, Object> result = Post.api(tokenApi, bodyParams);

        if (result == null)
            throw new RuntimeException("获取 JWT Token 失败");
        else {
            if ((int) result.get("status") == 0)
                throw new RuntimeException(result.get("message").toString());
            else {
                Map<String, Object> map = (Map<String, Object>) result.get("data");
                JwtAccessToken token = JsonUtil.map2pojo(map, JwtAccessToken.class);

                return token;
            }
        }
    }
}
