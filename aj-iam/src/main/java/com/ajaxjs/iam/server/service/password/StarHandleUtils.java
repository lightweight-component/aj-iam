package com.ajaxjs.iam.server.service.password;

import com.ajaxjs.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.StringJoiner;

/**
 * 使用星号（{@code *}）隐藏敏感信息的工具类。
 *
 * <p>提供 IPv4 地址、手机号、邮箱、身份证号码、姓名等信息的脱敏方法，
 * 以及生成星号字符串、截取字符串左右部分的辅助方法。脱敏仅用于展示层隐藏信息，
 * 不属于加密操作，不能替代敏感数据的安全存储或访问控制。</p>
 *
 * <p>接收敏感信息字符串的脱敏方法在输入为 {@code null}、空字符串或仅含空白字符时，
 * 均返回空字符串；但 {@link #getLeft(String, int)} 和 {@link #getRight(String, int)}
 * 不进行空值判断。输入不会自动去除首尾空白，也不会进行完整的业务格式校验。</p>
 *
 * <p>字符串长度和截取位置均基于 Java 的 UTF-16 代码单元，而非 Unicode 码点；
 * 对包含表情等补充字符的文本，截取时可能拆分代理项对。</p>
 *
 * @author Erwin Feng
 * @since 2020/8/23 1:57 上午
 */
@Slf4j
public class StarHandleUtils {
    /**
     * 隐藏 IPv4 地址的后两段，保留前两段。
     *
     * <p>例如，{@code 192.168.1.100} 处理后为 {@code 192.168.*.*}。
     * 本方法仅按英文句点拆分并检查结果是否为四段，不校验各段是否为数字或是否在
     * 0～255 范围内，也不支持 IPv6。拆分采用 {@link String#split(String)} 的默认行为，
     * 末尾空段会被丢弃。</p>
     *
     * @param ip 待脱敏的 IPv4 地址字符串
     * @return 后两段替换为星号的地址；输入无有效文本或拆分结果不是四段时返回空字符串
     */
    public static String ip(String ip) {
        if (!StringUtils.hasText(ip))
            return CommonConstant.EMPTY_STRING;

        String[] array = ip.split("\\.");

        if (array.length == 4)
            return String.format("%s.%s.*.*", array[0], array[1]);

        return CommonConstant.EMPTY_STRING;
    }

    /**
     * 隐藏 11 位手机号的中间四位，保留前三位和后四位。
     *
     * <p>例如，{@code 12345678901} 处理后为 {@code 123****8901}。
     * 仅检查字符串长度是否为 11，不校验是否全部为数字或是否为有效手机号。</p>
     *
     * @param phone 待脱敏的手机号字符串，长度应为 11
     * @return 脱敏后的手机号；输入无有效文本或长度不为 11 时返回空字符串
     */
    public static String phone(String phone) {
        if (!StringUtils.hasText(phone))
            return CommonConstant.EMPTY_STRING;

        if (phone.length() == 11) {
            String left = getLeft(phone, 3);
            String right = getRight(phone, 4);
            String star = generateStar(4);

            return left + star + right;
        }

        return CommonConstant.EMPTY_STRING;
    }

    /**
     * 隐藏邮箱中最后一个 {@code @} 之前的用户名部分。
     *
     * <p>用户名长度大于 1 时保留其首字符，其余字符逐个替换为星号；
     * 用户名只有一个字符时，该字符也会被替换。最后一个 {@code @} 及其后面的内容原样保留。
     * 例如，{@code alice@example.com} 处理后为 {@code a****@example.com}，
     * {@code a@example.com} 处理后为 {@code *@example.com}。</p>
     *
     * <p>不进行完整的邮箱格式校验；即使包含多个 {@code @} 或域名为空，
     * 只要最后一个 {@code @} 前存在字符，仍会按上述规则处理。</p>
     *
     * @param email 待脱敏的邮箱字符串
     * @return 脱敏后的邮箱；输入无有效文本、不含 {@code @} 或最后一个 {@code @}
     * 位于首位时返回空字符串
     */
    public static String email(String email) {
        if (!StringUtils.hasText(email))
            return CommonConstant.EMPTY_STRING;

        int index = email.lastIndexOf("@");

        if (index > 0) {
            int indexLeft = index == 1 ? 0 : 1;
            String right = getRight(email, email.length() - index);
            String left = getLeft(email, indexLeft);
            String star = generateStar(index - indexLeft);

            return left + star + right;
        }

        return CommonConstant.EMPTY_STRING;
    }

    /**
     * 使用默认规则隐藏身份证号码，保留前三位和后四位。
     *
     * <p>中间每个字符替换为一个星号，输出长度与输入相同。
     * 例如，{@code 110101199001011234} 处理后为 {@code 110***********1234}。
     * 本方法委托给 {@link #idCardNo(String, int, int)}，不校验证件位数、字符或校验码。</p>
     *
     * @param idCardNo 待脱敏的身份证号码字符串
     * @return 脱敏后的号码；输入无有效文本或长度不大于 7 时返回空字符串
     */
    public static String idCardNo(String idCardNo) {
        return idCardNo(idCardNo, 3, 4);
    }

    /**
     * 根据指定的左右保留长度隐藏身份证号码的中间部分。
     *
     * <p>保留左侧 {@code leftLength} 个字符和右侧 {@code rightLength} 个字符，
     * 中间每个字符替换为一个星号。对于合法的保留长度，输出长度与输入相同。
     * 不校验身份证号码的业务格式。</p>
     *
     * <p>左右保留长度均应为非负数，且两者之和不应发生整数溢出。
     * 本方法不会主动校验这些参数；负数在执行到截取操作时可能导致索引越界异常。</p>
     *
     * @param idCardNo    待脱敏的身份证号码字符串
     * @param leftLength  左侧保留的字符数，应为非负数
     * @param rightLength 右侧保留的字符数，应为非负数
     * @return 脱敏后的号码；输入无有效文本，或长度不大于左右保留长度之和时返回空字符串
     */
    public static String idCardNo(String idCardNo, int leftLength, int rightLength) {
        if (!StringUtils.hasText(idCardNo))
            return CommonConstant.EMPTY_STRING;

        if (idCardNo.length() <= leftLength + rightLength)
            return CommonConstant.EMPTY_STRING;

        String leftString = getLeft(idCardNo, leftLength);
        String rightString = getRight(idCardNo, rightLength);
        int starLength = idCardNo.length() - leftLength - rightLength;
        String starString = generateStar(starLength);

        return leftString + starString + rightString;
    }

    /**
     * 生成用于密码展示占位的固定星号字符串。
     *
     * <p>始终返回 32 个星号，不接收或读取真实密码，也不反映真实密码的长度。
     * 返回值仅用于界面展示，不应作为密码散列或实际认证凭据。</p>
     *
     * @return 由 32 个星号组成的字符串
     */
    public static String password() {
        return generateStar(32);
    }

    /**
     * 根据姓名长度隐藏部分字符。
     *
     * <ul>
     *     <li>一个字符：替换为一个星号，例如 {@code 张} 变为 {@code *}。</li>
     *     <li>两个字符：隐藏首字符，保留末字符，例如 {@code 张三} 变为 {@code *三}。</li>
     *     <li>三个及以上字符：保留首尾字符，中间逐个替换为星号，
     *     例如 {@code 张小三} 变为 {@code 张*三}。</li>
     * </ul>
     *
     * @param realName 待脱敏的真实姓名，不会自动去除空白或分隔符
     * @return 脱敏后的姓名；输入无有效文本时返回空字符串
     */
    public static String realName(String realName) {
        if (!StringUtils.hasText(realName))
            return CommonConstant.EMPTY_STRING;

        if (realName.length() == 2) {
            String right = getRight(realName, 1);
            String star = generateStar(1);
            return star + right;
        }

        if (realName.length() > 2) {
            String left = getLeft(realName, 1);
            String right = getRight(realName, 1);
            String star = generateStar(realName.length() - 2);
            return left + star + right;
        }

        return generateStar(1);
    }

    /**
     * 生成指定数量的星号（{@code *}），星号之间不添加分隔符。
     *
     * <p>例如，长度为 4 时返回 {@code ****}。长度为零或负数时，
     * 不执行追加操作，返回空字符串。</p>
     *
     * @param length 要生成的星号数量
     * @return 包含指定数量星号的字符串；{@code length <= 0} 时返回空字符串
     */
    public static String generateStar(int length) {
        StringJoiner sj = new StringJoiner(CommonConstant.EMPTY_STRING, CommonConstant.EMPTY_STRING, CommonConstant.EMPTY_STRING);

        for (int i = 0; i < length; i++)
            sj.add("*");

        return sj.toString();
    }

    /**
     * 截取字符串左侧指定长度的内容。
     *
     * <p>例如，{@code getLeft("张三", 1)} 返回 {@code 张}。
     * 当请求长度超过原字符串长度时，记录一条 INFO 日志并返回整个字符串；
     * 长度为零时返回空字符串。本方法不检查输入是否包含有效文本。</p>
     *
     * @param source 待截取的字符串，不能为 {@code null}
     * @param length 从左侧截取的字符数，应为非负数
     * @return 左侧子字符串；请求长度超过原字符串长度时返回完整内容
     * @throws NullPointerException      当 {@code source} 为 {@code null} 时
     * @throws IndexOutOfBoundsException 当 {@code length} 为负数时
     */
    public static String getLeft(String source, int length) {
        if (length > source.length()) {
            log.info("length(" + length + ") > source's length(" + source.length() + ")");
            length = source.length();
        }

        return source.substring(0, length);
    }

    /**
     * 截取字符串右侧指定长度的内容。
     *
     * <p>例如，{@code getRight("张三", 1)} 返回 {@code 三}。
     * 当请求长度超过原字符串长度时，记录一条 INFO 日志并返回整个字符串；
     * 长度为零时返回空字符串。本方法不检查输入是否包含有效文本。</p>
     *
     * @param source 待截取的字符串，不能为 {@code null}
     * @param length 从右侧截取的字符数，应为非负数
     * @return 右侧子字符串；请求长度超过原字符串长度时返回完整内容
     * @throws NullPointerException      当 {@code source} 为 {@code null} 时
     * @throws IndexOutOfBoundsException 当 {@code length} 为负数时
     */
    public static String getRight(String source, int length) {
        if (length > source.length()) {
            log.info("length(" + length + ") > source's length(" + source.length() + ")");
            length = source.length();
        }

        return source.substring(source.length() - length);
    }

}