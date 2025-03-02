[![Maven Central](https://img.shields.io/maven-central/v/com.ajaxjs/ajaxjs-data?label=Latest%20Release)](https://central.sonatype.com/artifact/com.ajaxjs/ajaxjs-data)
[![Javadoc](https://img.shields.io/badge/javadoc-1.1.6-brightgreen.svg?)](https://dev.ajaxjs.com/docs/javadoc/aj-data/)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:frank@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/static/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

# aj-iam-client

OAuth  的 客户端 Client

Tutorial: https://framework.ajaxjs.com/docs/aj/?section=json.

Java Documents: https://dev.ajaxjs.com/docs/javadoc/aj-json/.

# Install
Requires Java 1.8+, Maven Snippets:

```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-json</artifactId>
    <version>1.4</version>
</dependency>
```

# Usage

```java
@Value("${auth.excludes: }")
private String excludes;

/**
 * 加入认证拦截器
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {
    LogHelper.p("初始化 SSO 拦截器");
    InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor());
    interceptorRegistration.addPathPatterns("/**"); // 拦截所有

    // 不需要的拦截路径
    if (StringUtils.hasText(excludes)) {
        String[] arr = excludes.split("\\|");
        interceptorRegistration.excludePathPatterns(arr);
    }
}
```