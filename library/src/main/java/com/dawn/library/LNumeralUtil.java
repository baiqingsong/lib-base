package com.dawn.library;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 数字/金额工具类
 */
@SuppressWarnings("unused")
public class LNumeralUtil {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d{1,3}$");

    /** 金额最大值 */
    public static final BigDecimal MAX_AMOUNT = new BigDecimal("99999.99");

    /**
     * 校验金额是否正确
     * @param money 输入的金额
     * @return 是否正确
     */
    public static boolean isValid(String money) {
        if (money == null || money.trim().isEmpty()) return false;
        if (!AMOUNT_PATTERN.matcher(money).matches()) return false;
        try {
            BigDecimal amount = new BigDecimal(money);
            return amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(MAX_AMOUNT) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 字符串转换成Double，保留2位小数
     * @param money 输入的金额
     * @return 转换后的Double，失败返回null
     */
    @SuppressWarnings("deprecation")
    public static Double convertToDouble(String money) {
        try {
            BigDecimal bd = new BigDecimal(money);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 2个数相除，保留2位小数
     * @param number1 被除数
     * @param number2 除数
     * @return 相除结果字符串（除数为0返回空串）
     */
    public static String divide(int number1, int number2) {
        if (number2 == 0) return "";
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double) number1 / number2);
    }

    /**
     * 校验是否为3位以内数字
     * @param str 数字字符串
     * @return 是否匹配
     */
    public static boolean isNumeric(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 校验数字限制
     * @param num 数字
     * @param minNumber 最小数（包含）
     * @param maxDigit 最大位数
     * @return 是否合法
     */
    public static boolean isNumeric(int num, int minNumber, int maxDigit) {
        if (minNumber < 0 || maxDigit <= 0) return false;
        String s = String.valueOf(num);
        String regex = "^\\d{1," + maxDigit + "}$";
        Pattern pattern = Pattern.compile(regex);
        return num >= minNumber && pattern.matcher(s).matches();
    }
}
