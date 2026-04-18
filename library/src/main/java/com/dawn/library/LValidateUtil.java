package com.dawn.library;

import java.util.regex.Pattern;

/**
 * 验证工具类
 */
@SuppressWarnings("unused")
public class LValidateUtil {

    // 常用正则表达式
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static final String REGEX_PHONE = "^1[3-9]\\d{9}$";
    private static final String REGEX_ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private static final String REGEX_URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final String REGEX_IP = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

    /**
     * 验证邮箱格式
     * @param email 邮箱地址
     * @return 是否为有效邮箱
     */
    public static boolean isValidEmail(String email) {
        return !LStringUtil.isEmpty(email) && Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 验证手机号格式
     * @param phone 手机号
     * @return 是否为有效手机号
     */
    public static boolean isValidPhone(String phone) {
        return !LStringUtil.isEmpty(phone) && Pattern.matches(REGEX_PHONE, phone);
    }

    /**
     * 验证身份证号格式
     * @param idCard 身份证号
     * @return 是否为有效身份证号
     */
    public static boolean isValidIdCard(String idCard) {
        if (LStringUtil.isEmpty(idCard) || idCard.length() != 18) {
            return false;
        }
        return Pattern.matches(REGEX_ID_CARD, idCard) && validateIdCardChecksum(idCard);
    }

    /**
     * 验证身份证校验位
     * @param idCard 身份证号
     * @return 校验位是否正确
     */
    private static boolean validateIdCardChecksum(String idCard) {
        try {
            char[] chars = idCard.toUpperCase().toCharArray();
            int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            char[] parity = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += (chars[i] - '0') * factor[i];
            }
            return chars[17] == parity[sum % 11];
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证URL格式
     * @param url URL地址
     * @return 是否为有效URL
     */
    public static boolean isValidUrl(String url) {
        return !LStringUtil.isEmpty(url) && Pattern.matches(REGEX_URL, url);
    }

    /**
     * 验证IP地址格式
     * @param ip IP地址
     * @return 是否为有效IP地址
     */
    public static boolean isValidIP(String ip) {
        return !LStringUtil.isEmpty(ip) && Pattern.matches(REGEX_IP, ip);
    }

    /**
     * 验证密码强度（至少8位，包含大小写字母和数字）
     * @param password 密码
     * @return 是否为强密码
     */
    public static boolean isStrongPassword(String password) {
        return !LStringUtil.isEmpty(password) && Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 验证银行卡号（简单验证）
     * @param bankCard 银行卡号
     * @return 是否为有效银行卡号
     */
    public static boolean isValidBankCard(String bankCard) {
        if (LStringUtil.isEmpty(bankCard) || bankCard.length() < 16 || bankCard.length() > 19) {
            return false;
        }
        return bankCard.matches("^\\d+$");
    }

    /**
     * 验证车牌号
     * @param plateNumber 车牌号
     * @return 是否为有效车牌号
     */
    public static boolean isValidPlateNumber(String plateNumber) {
        String regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$";
        return !LStringUtil.isEmpty(plateNumber) && Pattern.matches(regex, plateNumber);
    }

    /**
     * 验证QQ号
     * @param qq QQ号
     * @return 是否为有效QQ号
     */
    public static boolean isValidQQ(String qq) {
        String regex = "^[1-9][0-9]{4,10}$";
        return !LStringUtil.isEmpty(qq) && Pattern.matches(regex, qq);
    }

    /**
     * 验证微信号
     * @param wechat 微信号
     * @return 是否为有效微信号
     */
    public static boolean isValidWechat(String wechat) {
        String regex = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$";
        return !LStringUtil.isEmpty(wechat) && Pattern.matches(regex, wechat);
    }

    /**
     * 验证邮政编码
     * @param zipCode 邮政编码
     * @return 是否为有效邮政编码
     */
    public static boolean isValidZipCode(String zipCode) {
        String regex = "^[1-9]\\d{5}$";
        return !LStringUtil.isEmpty(zipCode) && Pattern.matches(regex, zipCode);
    }

    /**
     * 格式化手机号（中间四位用*代替）
     * @param phone 手机号
     * @return 格式化后的手机号
     */
    public static String formatPhone(String phone) {
        if (!isValidPhone(phone)) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 格式化身份证号（中间部分用*代替）
     * @param idCard 身份证号
     * @return 格式化后的身份证号
     */
    public static String formatIdCard(String idCard) {
        if (!isValidIdCard(idCard)) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 格式化银行卡号（中间部分用*代替）
     * @param bankCard 银行卡号
     * @return 格式化后的银行卡号
     */
    public static String formatBankCard(String bankCard) {
        if (!isValidBankCard(bankCard)) {
            return bankCard;
        }
        int length = bankCard.length();
        if (length <= 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "****" + "****" + bankCard.substring(length - 4);
    }

    /**
     * 格式化邮箱（用户名部分用*代替）
     * @param email 邮箱地址
     * @return 格式化后的邮箱
     */
    public static String formatEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        
        if (username.length() <= 2) {
            return email;
        }
        
        return username.substring(0, 1) + "***" + username.substring(username.length() - 1) + domain;
    }
}
