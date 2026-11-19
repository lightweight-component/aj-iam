package com.ajaxjs.iam.server.service.password;

import com.ajaxjs.util.CommonConstant;

import java.util.Random;

/**
 * 生成指定长度、包含四类字符的随机密码候选字符串。
 *
 * <p>四类字符分别为 ASCII 大写字母、小写字母、数字和预定义的特殊字符。
 * 各类字符至少出现一次，但同一字符可以重复出现。</p>
 *
 * <p>本类使用 {@link Random}，不提供密码学安全的随机性。
 * 生成真实账户密码、临时凭据或令牌时，应使用基于
 * {@link java.security.SecureRandom} 的生成方案。</p>
 *
 * @author <a href="https://www.zifangsky.cn/1540.html">原实现来源</a>
 */
public class MakePassword {
    /**
     * 生成密码时使用的特殊字符集合；不包含空白字符。
     */
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    /**
     * 随机查找一个尚未填充的位置。
     *
     * <p>值为 {@code '\0'} 的元素视为空位。如果抽到已填充的位置，则重新抽取。
     * 调用方须保证至少有一个空位，否则非空数组上的查找不会结束。</p>
     *
     * @param chars 待填充的非空字符数组，至少包含一个空位
     * @param rnd   用于选择下标的随机数生成器
     * @return 一个尚未填充的位置下标（从 0 开始）
     */
    private static int nextIndex(char[] chars, Random rnd) {
        int index = rnd.nextInt(chars.length);

        while (chars[index] != 0)
            index = rnd.nextInt(chars.length);

        return index;
    }

    /**
     * 从 {@link #SPECIAL_CHARS} 中随机选取一个字符。
     *
     * @param rnd 用于选取字符的随机数生成器
     * @return 预定义集合中的一个特殊字符
     */
    private static char nextSpecialChar(Random rnd) {
        return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
    }

    /**
     * 从 ASCII 大写字母 {@code A} 至 {@code Z} 中随机选取一个字符。
     *
     * @param rnd 用于选取字母的随机数生成器
     * @return 一个大写字母
     */
    private static char nextUpperLetter(Random rnd) {
        return (char) ('A' + rnd.nextInt(26));
    }

    /**
     * 从 ASCII 小写字母 {@code a} 至 {@code z} 中随机选取一个字符。
     *
     * @param rnd 用于选取字母的随机数生成器
     * @return 一个小写字母
     */
    private static char nextLowerLetter(Random rnd) {
        return (char) ('a' + rnd.nextInt(26));
    }

    /**
     * 从 ASCII 数字字符 {@code 0} 至 {@code 9} 中随机选取一个字符。
     *
     * @param rnd 用于选取数字字符的随机数生成器
     * @return 一个数字字符，而不是对应的整数值
     */
    private static char nextNumLetter(Random rnd) {
        return (char) ('0' + rnd.nextInt(10));
    }

    /**
     * 先等概率选择一种字符类别，再从该类别中随机选取一个字符。
     *
     * <p>四类字符的集合大小不同，因此并非在所有可用字符中等概率抽取。</p>
     *
     * @param rnd 用于选择类别和字符的随机数生成器
     * @return 一个小写字母、大写字母、数字字符或特殊字符
     */
    private static char nextChar(Random rnd) {
        switch (rnd.nextInt(4)) {
            case 0:
                return (char) ('a' + rnd.nextInt(26));
            case 1:
                return (char) ('A' + rnd.nextInt(26));
            case 2:
                return (char) ('0' + rnd.nextInt(10));
            default:
                return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
        }
    }

    /**
     * 生成指定长度的随机密码候选字符串。
     *
     * <p>先将大写字母、小写字母、数字和特殊字符各一个放入不同的随机位置，
     * 再随机填充其余位置。长度为 4 时，结果恰好包含每类字符各一个；
     * 长度大于 4 时，同类字符或同一字符可以出现多次。</p>
     *
     * <p>例如，传入 {@code 4} 时，可能得到 {@code aB3!}。此方法仅保证长度和字符类别，
     * 不验证其他密码策略，也不保证多次调用的结果互不相同。</p>
     *
     * @param length 期望生成的字符数，最小有效值为 4
     * @return 指定长度的密码候选字符串；{@code length < 4} 时返回空字符串
     */
    public static String randomPassword(int length) {
        if (length < 4)
            return CommonConstant.EMPTY_STRING;

        char[] chars = new char[length];
        Random rnd = new Random();

        // 1. 至少生成一个大写字母、小写字母、特殊字符、数字
        chars[nextIndex(chars, rnd)] = nextSpecialChar(rnd);
        chars[nextIndex(chars, rnd)] = nextUpperLetter(rnd);
        chars[nextIndex(chars, rnd)] = nextLowerLetter(rnd);
        chars[nextIndex(chars, rnd)] = nextNumLetter(rnd);

        // 2. 填补其他位置的字符
        for (int i = 0; i < length; i++) {
            if (chars[i] == 0)
                chars[i] = nextChar(rnd);
        }

        // 3. 返回结果
        return new String(chars);
    }
}
