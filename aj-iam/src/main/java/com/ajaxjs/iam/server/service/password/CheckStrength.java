package com.ajaxjs.iam.server.service.password;

/**
 * 根据密码长度、字符组合和常见弱模式估算密码强度。
 *
 * <p>可通过 {@link #checkPasswordStrength(String)} 获取原始分数，或通过
 * {@link #getPasswordLevel(String)} 获取适合展示的等级。分数是基于固定规则的估算，
 * 不代表密码的信息熵，也不能替代密码策略校验或泄露密码检查。</p>
 *
 * <p>长度和字符计数均以 UTF-16 代码单元为单位。评分时仅区分 ASCII 数字、
 * ASCII 大小写字母和其他字符；空格、标点及非 ASCII 字符均归入其他字符。</p>
 *
 * @author <a href="https://blog.csdn.net/weixin_43627706/article/details/116165365">原实现来源</a>
 */
public class CheckStrength {
    /**
     * 按原始分数从低到高划分的密码强度等级。
     */
    public enum LEVEL {
        /**
         * 简单，0～3 分。
         */
        EASY("简单"),
        /**
         * 中等，4～6 分。
         */
        MEDIUM("中等"),
        /**
         * 强，7～9 分。
         */
        STRONG("强"),
        /**
         * 非常强，10～12 分。
         */
        VERY_STRONG("非常强"),
        /**
         * 极强，13 分及以上。
         */
        EXTREMELY_STRONG("极强");

        /**
         * 获取当前等级的展示文案。
         *
         * @return 当前文案；如果曾被设置为 {@code null}，则返回 {@code null}
         */
        public String getMessage() {
            return message;
        }

        /**
         * 修改当前等级的展示文案，不影响分数与等级之间的映射。
         *
         * <p>枚举常量是共享实例，此操作并非为单次调用设置文案。</p>
         *
         * @param message 新文案，不校验内容，允许为 {@code null}
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * 当前等级的展示文案。
         */
        private String message;

        /**
         * 初始化等级的默认展示文案。
         *
         * @param message 默认文案
         */
        LEVEL(String message) {
            this.message = message;
        }
    }

    /**
     * ASCII 数字类别。
     */
    private static final int NUM = 1;
    /**
     * ASCII 小写字母类别。
     */
    private static final int SMALL_LETTER = 2;
    /**
     * ASCII 大写字母类别。
     */
    private static final int CAPITAL_LETTER = 3;
    /**
     * 不属于上述三类的字符类别。
     */
    private static final int OTHER_CHAR = 4;

    /**
     * 用于评分扣分的内置弱密码样本，匹配时区分大小写。
     * 当某个样本包含整个待评估密码时即命中，完全相等也算命中。
     */
    private final static String[] DICTIONARY = {"password", "abc123", "iloveyou", "adobe123", "123123", "1234567890", "1314520", "a1b2c3", "123qwe",
            "aaa111", "qweasd", "admin", "passwd"};

    /**
     * 判断单个 UTF-16 代码单元所属的评分字符类别。
     *
     * @param c 待分类的字符
     * @return 数字、小写字母、大写字母或其他字符对应的类别常量
     */
    private static int checkCharacterType(char c) {
        if (c >= 48 && c <= 57)
            return NUM;

        if (c >= 65 && c <= 90)
            return CAPITAL_LETTER;

        if (c >= 97 && c <= 122)
            return SMALL_LETTER;

        return OTHER_CHAR;
    }

    /**
     * 统计密码中属于指定类别的字符数量。
     *
     * @param passwd 待统计的密码，可以为 {@code null}
     * @param type   {@link #checkCharacterType(char)} 返回的类别值
     * @return 匹配的字符数量；密码为 {@code null} 或空字符串时返回 0
     */
    private static int countLetter(String passwd, int type) {
        int count = 0;

        if (null != passwd && !passwd.isEmpty()) {
            for (char c : passwd.toCharArray()) {
                if (checkCharacterType(c) == type)
                    count++;
            }
        }

        return count;
    }

    /**
     * 根据内置规则计算密码强度分数。
     *
     * <p>长度、字符类别的组合和各类字符的数量都可能加分。部分连续字母、键盘或数字序列，
     * 单一字符类别、重复片段、疑似生日和内置字典命中都可能扣分。</p>
     *
     * <p>输入不会去除首尾空白。长度不超过 3 或被 {@link #isCharEqual(String)}
     * 判为重复的密码直接归零，负分也归零。若需要展示等级，请使用
     * {@link #getPasswordLevel(String)}。</p>
     *
     * @param passwd 待评估的密码原文
     * @return 非负的原始强度分数
     * @throws IllegalArgumentException 当 {@link #equalsNull(String)} 判定密码为空时
     * @throws NumberFormatException    当疑似日期检查中的数字片段超出 {@code int} 的取值范围时
     */
    public static int checkPasswordStrength(String passwd) {
        if (equalsNull(passwd))
            throw new IllegalArgumentException("password is empty");

        int len = passwd.length();
        int level = 0;

        // 增加点
        // 判断密码是否含有数字有level++
        if (countLetter(passwd, NUM) > 0)
            level++;

        // 判断密码是否含有小写字母有level++
        if (countLetter(passwd, SMALL_LETTER) > 0)
            level++;

        // 判断密码是否还有大写字母有level++
        if (len > 4 && countLetter(passwd, CAPITAL_LETTER) > 0)
            level++;

        // 判断密码是否还有特殊字符有level++
        if (len > 6 && countLetter(passwd, OTHER_CHAR) > 0)
            level++;

        // 密码长度大于4并且2种类型组合......（不一一概述）
        if (len > 4 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        // 密码长度大于6并且3中类型组合......（不一一概述）
        if (len > 6 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        // 密码长度大于8并且4种类型组合......（不一一概述）
        if (len > 8 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }

        // 密码长度大于6并且2种类型组合每种类型长度大于等于3或者2......（不一一概述）
        if (len > 6 && countLetter(passwd, NUM) >= 3 && countLetter(passwd, SMALL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, CAPITAL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        // 密码长度大于8并且3种类型组合每种类型长度大于等于3或者2......（不一一概述）
        if (len > 8 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2
                || countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, NUM) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        // 密码长度大于10并且4种类型组合每种类型长度大于等于2......（不一一概述）
        if (len > 10 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2
                && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }

        // 特殊字符>=3 level++;
        if (countLetter(passwd, OTHER_CHAR) >= 3)
            level++;

        // 特殊字符>=6 level++;
        if (countLetter(passwd, OTHER_CHAR) >= 6)
            level++;

        // 长度>12 >16 level++
        if (len > 12) {
            level++;
            if (len >= 16)
                level++;
        }

        // 减少点
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(passwd) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(passwd) > 0)
            level--;

        if ("qwertyuiop".indexOf(passwd) > 0 || "asdfghjkl".indexOf(passwd) > 0 || "zxcvbnm".indexOf(passwd) > 0)
            level--;

        if (isNumeric(passwd) && ("01234567890".indexOf(passwd) > 0 || "09876543210".indexOf(passwd) > 0))
            level--;

        if (countLetter(passwd, NUM) == len || countLetter(passwd, SMALL_LETTER) == len || countLetter(passwd, CAPITAL_LETTER) == len)
            level--;

        if (len % 2 == 0) { // aaabbb
            String part1 = passwd.substring(0, len / 2);
            String part2 = passwd.substring(len / 2);

            if (part1.equals(part2))
                level--;

            if (isCharEqual(part1) && isCharEqual(part2))
                level--;
        }

        if (len % 3 == 0) { // ababab
            String part1 = passwd.substring(0, len / 3);
            String part2 = passwd.substring(len / 3, len / 3 * 2);
            String part3 = passwd.substring(len / 3 * 2);

            if (part1.equals(part2) && part2.equals(part3))
                level--;
        }

        if (isNumeric(passwd) && len >= 6) { // 19881010 or 881010
            int year = 0;
            if (len == 8 || len == 6)
                year = Integer.parseInt(passwd.substring(0, len - 4));

            int size = sizeOfInt(year);
            int month = Integer.parseInt(passwd.substring(size, size + 2));
            int day = Integer.parseInt(passwd.substring(size + 2, len));

            if (year >= 1950 && year < 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31)
                level--;
        }

        if (null != DICTIONARY) {// dictionary
            for (String s : DICTIONARY) {
                if (passwd.equals(s) || s.contains(passwd)) {
                    level--;
                    break;
                }
            }
        }

        if (len <= 6) {
            level--;
            if (len <= 4) {
                level--;
                if (len <= 3)
                    level = 0;
            }
        }

        if (isCharEqual(passwd))
            level = 0;

        if (level < 0)
            level = 0;

        return level;
    }

    /**
     * 计算密码强度并将分数映射为五档展示等级。
     *
     * <p>0～3 分为简单，4～6 分为中等，7～9 分为强，10～12 分为非常强，
     * 13 分及以上为极强。</p>
     *
     * @param passwd 待评估的密码原文
     * @return 与评分区间对应的等级
     * @throws IllegalArgumentException 当 {@link #equalsNull(String)} 判定密码为空时
     * @throws NumberFormatException    当疑似日期检查中的数字片段超出 {@code int} 的取值范围时
     */
    public static LEVEL getPasswordLevel(String passwd) {
        int level = checkPasswordStrength(passwd);

        switch (level) {
            case 0:
            case 1:
            case 2:
            case 3:
                return LEVEL.EASY;
            case 4:
            case 5:
            case 6:
                return LEVEL.MEDIUM;
            case 7:
            case 8:
            case 9:
                return LEVEL.STRONG;
            case 10:
            case 11:
            case 12:
                return LEVEL.VERY_STRONG;
            default:
                return LEVEL.EXTREMELY_STRONG;
        }
    }

    /**
     * 非负整数在各十进制位数下的上界，最后一项为 {@link Integer#MAX_VALUE}。
     */
    private final static int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

    /**
     * 通过查表计算非负整数的十进制位数。
     *
     * @param x 待计算位数的整数
     * @return 非负整数的十进制位数（0 为一位）；负数也返回 1，不计算其绝对值
     */
    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++) {
            if (x <= SIZE_TABLE[i])
                return i + 1;
        }
    }

    /**
     * 判断字符串是否满足评分使用的重复字符规则。
     *
     * <p>将所有与首字符相同的字符替换为空格，再通过 {@link String#trim()}
     * 去除首尾空白；结果为空即判为重复。因此它不是严格的逐字符相等判断，
     * 例如 {@code "aa a"} 也会返回 {@code true}。</p>
     *
     * @param str 待检查的非空字符串
     * @return 替换并去除首尾空白后是否为空字符串
     * @throws NullPointerException      当 {@code str} 为 {@code null} 时
     * @throws IndexOutOfBoundsException 当 {@code str} 为空字符串时
     */
    public static boolean isCharEqual(String str) {
        return str.replace(str.charAt(0), ' ').trim().length() == 0;
    }

    /**
     * 判断字符串中的每个代码单元是否均为数字字符。
     *
     * <p>按 {@link Character#isDigit(char)} 判断，允许部分非 ASCII 数字，
     * 不接受正负号、小数点或空白。不解析数值，也不检查数值范围。</p>
     *
     * @param str 待检查的字符串，不能为 {@code null}
     * @return 所有字符均为数字时返回 {@code true}；空字符串也返回 {@code true}
     * @throws NullPointerException 当 {@code str} 为 {@code null} 时
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }

        return true;
    }

    /**
     * 判断输入是否应在密码评分前视为空值。
     *
     * <p>{@code null}、空字符串、忽略大小写后等于字面量 {@code "null"} 的字符串，
     * 或完全由 {@link Character#isWhitespace(char)} 识别的空白字符组成的字符串，
     * 均视为空值。不会先去除首尾空白再匹配字面量，因此 {@code " null "} 不视为空值。</p>
     *
     * @param str 待检查的字符串，可以为 {@code null}
     * @return 输入是否应视为空值
     */
    public static boolean equalsNull(String str) {
        int strLen;

        if (str == null || (strLen = str.length()) == 0 || str.equalsIgnoreCase("null"))
            return true;

        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i))))
                return false;
        }

        return true;
    }
}
