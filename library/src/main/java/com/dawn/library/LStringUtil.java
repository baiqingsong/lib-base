package com.dawn.library;

import java.text.DecimalFormat;

/**
 * 字符串工具类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LStringUtil {

    /**
     * 将null转化成""
     * @param str 处理的字符串
     *
     * @return String 处理后的字符串
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    /**
     * 是否是空字符串
     * @param str 判断的字符串
     *
     * @return boolean 是否是空字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 字符串长度
     * @param str 需要判断的字符串
     *
     * @return int 字符串长度
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u4E00-\u9FA5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 是否全是中文
     * @param str 判断的字符串
     *
     * @return boolean 是否全是中文
     */
    public static boolean isChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String chinese = "[\u4E00-\u9FA5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (!temp.matches(chinese)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含中文
     * @param str 判断的字符串
     *
     * @return boolean 是否包含中文
     */
    public static boolean isContainChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String chinese = "[\u4E00-\u9FA5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定小数输出
     * @param s 处理的小数
     * @param format #.## 保留两位小数，可能少于两位小数。比实际位数多，不变。比实际位数少，整数不变东，小数部分，四舍五入
     *               0.00 保留两位小数，确定两位小数。比实际位数多，不足补0。比实际位数少，整数不改动，小数部分，四舍五入
     *
     * @return String 处理后的字符串
     */
    public static String decimalFormat(double s, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(s);
    }

    /**
     * 字节数组转为字符串
     * @param byteArray 字节数组
     *
     * @return String 字符串
     */
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for(byte b : byteArray){
            if ((b & 0xFF) < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString().toUpperCase();
    }

    /**
     * 字符串转为字节数组
     * @param hexString 字符串
     *
     * @return byte[] 字节数组
     */
    public static byte[] toByteArray(String hexString) {
        if (hexString == null)
            throw new IllegalArgumentException("this hexString must not be empty");
        if (hexString.length() % 2 != 0)
            throw new IllegalArgumentException("hexString length must be even");

        hexString = hexString.toUpperCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            // 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xFF);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xFF);
            byteArray[i] = (byte) (high << 4 | low & 0xFF);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 十进制转十六进制，少于两位补位
     * @param hex 十进制
     */
    public static String format(int hex) {
        String hexStr = Integer.toHexString(hex).toUpperCase();
        int len = hexStr.length();
        if (len < 2) {
            hexStr = "0" + hexStr;
        }
        return hexStr;
    }

    /**
     * 取反
     */
    public static String parseHex2Opposite(String str) {
        String hex;
        //十六进制转成二进制
        byte[] er = LStringUtil.toByteArray(str);
        //取反
        byte erBefore[] = new byte[er.length];
        for (int i = 0; i < er.length; i++) {
            erBefore[i] = (byte) ~er[i];
        }
        //二进制转成十六进制
        hex = LStringUtil.toHexString(erBefore);
        // 如果不够校验位的长度，补0,这里用的是两位校验
        hex = (hex.length() < 2 ? "0" + hex : hex);

        return hex;
    }

    /**
     * 异或
     * @param str 需要异或的十六进制字符串
     */
    public static String getXor(String str){
        byte[] bytes = toByteArray(str);
        byte temp = bytes[0];
        for (int i = 1; i <bytes.length; i++) {
            temp ^=bytes[i];
        }
        byte[] data = new byte[1];
        data[0] = temp;
        return toHexString(data);
    }

    /**
     * 首字母转大写
     * @param str 需要转换的字符串
     * @return 首字母大写的字符串
     */
    public static String capitalizeFirst(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母转小写
     * @param str 需要转换的字符串
     * @return 首字母小写的字符串
     */
    public static String lowercaseFirst(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 驼峰转下划线
     * @param str 驼峰字符串
     * @return 下划线分隔的字符串
     */
    public static String camelToUnderline(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String result = str.replaceAll("([A-Z])", "_$1").toLowerCase();
        if (result.startsWith("_")) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * 下划线转驼峰
     * @param str 下划线分隔的字符串
     * @return 驼峰字符串
     */
    public static String underlineToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        String[] parts = str.split("_");
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                result.append(parts[i].toLowerCase());
            } else {
                result.append(capitalizeFirst(parts[i].toLowerCase()));
            }
        }
        return result.toString();
    }

    /**
     * 判断字符串是否为数字
     * @param str 判断的字符串
     * @return 是否为数字
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为整数
     * @param str 判断的字符串
     * @return 是否为整数
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 移除字符串中的空格
     * @param str 需要处理的字符串
     * @return 移除空格后的字符串
     */
    public static String removeSpaces(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * 反转字符串
     * @param str 需要反转的字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 安全的equals比较（防止NPE）
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 是否相等
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == s2) return true;
        if (s1 == null || s2 == null) return false;
        return s1.equals(s2);
    }

    /**
     * 安全的equalsIgnoreCase比较
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 是否相等（忽略大小写）
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == s2) return true;
        if (s1 == null || s2 == null) return false;
        return s1.equalsIgnoreCase(s2);
    }

    /**
     * 如果为空则返回默认值
     * @param str 字符串
     * @param defaultValue 默认值
     * @return 非空字符串
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 拼接字符串数组
     * @param separator 分隔符
     * @param parts 字符串数组
     * @return 拼接后的字符串
     */
    public static String join(String separator, String... parts) {
        if (parts == null || parts.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(separator);
            sb.append(parts[i] != null ? parts[i] : "");
        }
        return sb.toString();
    }

    /**
     * 拼接集合
     * @param separator 分隔符
     * @param list 字符串集合
     * @return 拼接后的字符串
     */
    public static String join(String separator, java.util.List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(separator);
            String item = list.get(i);
            sb.append(item != null ? item : "");
        }
        return sb.toString();
    }

    /**
     * 重复字符串
     * @param str 需要重复的字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int count) {
        if (isEmpty(str) || count <= 0) return "";
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 截断字符串，超过最大长度时末尾添加"..."
     * @param str 原字符串
     * @param maxLength 最大长度（包含"..."）
     * @return 截断后的字符串
     */
    public static String abbreviate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) return str;
        if (maxLength <= 3) return str.substring(0, maxLength);
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * 忽略大小写判断是否包含
     * @param str 源字符串
     * @param search 搜索字符串
     * @return 是否包含
     */
    public static boolean containsIgnoreCase(String str, String search) {
        if (str == null || search == null) return false;
        return str.toLowerCase().contains(search.toLowerCase());
    }

    /**
     * 安全的substring（不会越界）
     * @param str 原字符串
     * @param start 开始索引
     * @param end 结束索引
     * @return 子字符串
     */
    public static String safeSubstring(String str, int start, int end) {
        if (isEmpty(str)) return str;
        if (start < 0) start = 0;
        if (end > str.length()) end = str.length();
        if (start >= end) return "";
        return str.substring(start, end);
    }

    /**
     * 安全的trim（防止NPE）
     * @param str 字符串
     * @return trim后的字符串
     */
    public static String safeTrim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 统计子字符串出现的次数
     * @param str 源字符串
     * @param sub 子字符串
     * @return 出现次数
     */
    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) return 0;
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(sub, index)) != -1) {
            count++;
            index += sub.length();
        }
        return count;
    }

    /**
     * 字符串左填充
     * @param str 原字符串
     * @param size 目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String padLeft(String str, int size, char padChar) {
        if (str == null) str = "";
        if (str.length() >= size) return str;
        StringBuilder sb = new StringBuilder(size);
        for (int i = str.length(); i < size; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 字符串右填充
     * @param str 原字符串
     * @param size 目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String padRight(String str, int size, char padChar) {
        if (str == null) str = "";
        if (str.length() >= size) return str;
        StringBuilder sb = new StringBuilder(size);
        sb.append(str);
        for (int i = str.length(); i < size; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否不为空
     * @param str 判断的字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 安全获取字符串，null返回空字符串
     * @param str 原字符串
     * @return 非null字符串
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 空字符串转null
     * @param str 原字符串
     * @return 空字符串返回null
     */
    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }

    /**
     * 截取字符串（安全截取，不会越界）
     * @param str 原字符串
     * @param start 开始位置
     * @param end 结束位置
     * @return 截取后的字符串
     */
    public static String substring(String str, int start, int end) {
        if (isEmpty(str)) return str;
        int len = str.length();
        if (start < 0) start = 0;
        if (end > len) end = len;
        if (start > end) return "";
        return str.substring(start, end);
    }

    /**
     * 隐藏手机号中间4位
     * @param phone 手机号
     * @return 隐藏后的手机号（如：138****1234）
     */
    public static String hidePhoneMiddle(String phone) {
        if (isEmpty(phone) || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 隐藏邮箱用户名部分
     * @param email 邮箱地址
     * @return 隐藏后的邮箱（如：a***b@example.com）
     */
    public static String hideEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) return email;
        int atIndex = email.indexOf("@");
        String name = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (name.length() <= 2) {
            return name.charAt(0) + "***" + domain;
        }
        return name.charAt(0) + "***" + name.charAt(name.length() - 1) + domain;
    }

    /**
     * 生成随机字符串
     * @param length 长度
     * @return 随机字符串（字母+数字）
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 字符串分割（支持多个分隔符）
     * @param str 原字符串
     * @param delimiters 分隔符字符串（每个字符都是分隔符）
     * @return 分割后的数组
     */
    public static String[] split(String str, String delimiters) {
        if (isEmpty(str)) return new String[0];
        if (isEmpty(delimiters)) return new String[]{str};
        return str.split("[" + java.util.regex.Pattern.quote(delimiters) + "]+");
    }

    /**
     * 统计子串出现次数（兼容别名）
     * @param str 原字符串
     * @param sub 子串
     * @return 出现次数
     */
    public static int countOccurrences(String str, String sub) {
        return countMatches(str, sub);
    }

}
