package com.dawn.library;

/**
 * 数据类型转换工具类
 */
@SuppressWarnings("unused")
public class LConvertUtil {

    /**
     * 字符串转int（转换失败返回默认值）
     * @param str 字符串
     * @param defaultValue 默认值
     * @return int值
     */
    public static int toInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转long（转换失败返回默认值）
     * @param str 字符串
     * @param defaultValue 默认值
     * @return long值
     */
    public static long toLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转float（转换失败返回默认值）
     * @param str 字符串
     * @param defaultValue 默认值
     * @return float值
     */
    public static float toFloat(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转double（转换失败返回默认值）
     * @param str 字符串
     * @param defaultValue 默认值
     * @return double值
     */
    public static double toDouble(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转boolean
     * @param str 字符串（支持 "true"、"1"、"yes"）
     * @param defaultValue 默认值
     * @return boolean值
     */
    public static boolean toBoolean(String str, boolean defaultValue) {
        if (str == null || str.isEmpty()) return defaultValue;
        String lower = str.trim().toLowerCase();
        if ("true".equals(lower) || "1".equals(lower) || "yes".equals(lower)) return true;
        if ("false".equals(lower) || "0".equals(lower) || "no".equals(lower)) return false;
        return defaultValue;
    }

    /**
     * int转byte数组（大端序）
     * @param value int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    /**
     * byte数组转int（大端序）
     * @param bytes byte数组
     * @return int值
     */
    public static int bytesToInt(byte[] bytes) {
        if (bytes == null || bytes.length < 4) return 0;
        return ((bytes[0] & 0xFF) << 24)
                | ((bytes[1] & 0xFF) << 16)
                | ((bytes[2] & 0xFF) << 8)
                | (bytes[3] & 0xFF);
    }

    /**
     * long转byte数组（大端序）
     * @param value long值
     * @return byte数组
     */
    public static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    /**
     * byte数组转long（大端序）
     * @param bytes byte数组
     * @return long值
     */
    public static long bytesToLong(byte[] bytes) {
        if (bytes == null || bytes.length < 8) return 0;
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (bytes[i] & 0xFF);
        }
        return value;
    }

    /**
     * 分转元
     * @param fen 金额（分）
     * @return 金额（元）字符串
     */
    public static String fenToYuan(long fen) {
        return String.format(java.util.Locale.getDefault(), "%.2f", fen / 100.0);
    }

    /**
     * 元转分
     * @param yuan 金额（元）字符串
     * @return 金额（分）
     */
    public static long yuanToFen(String yuan) {
        try {
            return Math.round(Double.parseDouble(yuan) * 100);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 摄氏度转华氏度
     * @param celsius 摄氏度
     * @return 华氏度
     */
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32;
    }

    /**
     * 华氏度转摄氏度
     * @param fahrenheit 华氏度
     * @return 摄氏度
     */
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }
}
