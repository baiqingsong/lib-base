package com.dawn.library;

import android.graphics.Color;

/**
 * 颜色工具类
 */
@SuppressWarnings("unused")
public class LColorUtil {

    /**
     * 颜色值转换为十六进制字符串
     * @param color 颜色值
     * @return 十六进制颜色字符串
     */
    public static String colorToHex(int color) {
        return String.format("#%08X", color);
    }

    /**
     * 十六进制颜色字符串转换为颜色值
     * @param hex 十六进制颜色字符串（如：#FFFFFF）
     * @return 颜色值
     */
    public static int hexToColor(String hex) {
        try {
            return Color.parseColor(hex);
        } catch (IllegalArgumentException e) {
            return Color.BLACK;
        }
    }

    /**
     * RGB转换为颜色值
     * @param red 红色分量 (0-255)
     * @param green 绿色分量 (0-255)
     * @param blue 蓝色分量 (0-255)
     * @return 颜色值
     */
    public static int rgbToColor(int red, int green, int blue) {
        return Color.rgb(red, green, blue);
    }

    /**
     * ARGB转换为颜色值
     * @param alpha 透明度 (0-255)
     * @param red 红色分量 (0-255)
     * @param green 绿色分量 (0-255)
     * @param blue 蓝色分量 (0-255)
     * @return 颜色值
     */
    public static int argbToColor(int alpha, int red, int green, int blue) {
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 获取颜色的红色分量
     * @param color 颜色值
     * @return 红色分量 (0-255)
     */
    public static int getRed(int color) {
        return Color.red(color);
    }

    /**
     * 获取颜色的绿色分量
     * @param color 颜色值
     * @return 绿色分量 (0-255)
     */
    public static int getGreen(int color) {
        return Color.green(color);
    }

    /**
     * 获取颜色的蓝色分量
     * @param color 颜色值
     * @return 蓝色分量 (0-255)
     */
    public static int getBlue(int color) {
        return Color.blue(color);
    }

    /**
     * 获取颜色的透明度分量
     * @param color 颜色值
     * @return 透明度分量 (0-255)
     */
    public static int getAlpha(int color) {
        return Color.alpha(color);
    }

    /**
     * 设置颜色的透明度
     * @param color 原颜色值
     * @param alpha 新透明度 (0-255)
     * @return 新颜色值
     */
    public static int setAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * 颜色混合
     * @param color1 颜色1
     * @param color2 颜色2
     * @param ratio 混合比例 (0.0-1.0)，0.0表示完全是color1，1.0表示完全是color2
     * @return 混合后的颜色
     */
    public static int blendColors(int color1, int color2, float ratio) {
        if (ratio < 0) ratio = 0;
        if (ratio > 1) ratio = 1;
        
        float inverseRatio = 1f - ratio;
        
        int r = (int) (Color.red(color1) * inverseRatio + Color.red(color2) * ratio);
        int g = (int) (Color.green(color1) * inverseRatio + Color.green(color2) * ratio);
        int b = (int) (Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio);
        int a = (int) (Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio);
        
        return Color.argb(a, r, g, b);
    }

    /**
     * 加深颜色
     * @param color 原颜色
     * @param factor 加深因子 (0.0-1.0)
     * @return 加深后的颜色
     */
    public static int darkenColor(int color, float factor) {
        if (factor < 0) factor = 0;
        if (factor > 1) factor = 1;
        
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (1f - factor);
        return Color.HSVToColor(Color.alpha(color), hsv);
    }

    /**
     * 变亮颜色
     * @param color 原颜色
     * @param factor 变亮因子 (0.0-1.0)
     * @return 变亮后的颜色
     */
    public static int lightenColor(int color, float factor) {
        if (factor < 0) factor = 0;
        if (factor > 1) factor = 1;
        
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] + (1f - hsv[2]) * factor;
        return Color.HSVToColor(Color.alpha(color), hsv);
    }

    /**
     * 判断颜色是否为深色
     * @param color 颜色值
     * @return 是否为深色
     */
    public static boolean isDarkColor(int color) {
        double brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255;
        return brightness < 0.5;
    }

    /**
     * 判断颜色是否为浅色
     * @param color 颜色值
     * @return 是否为浅色
     */
    public static boolean isLightColor(int color) {
        return !isDarkColor(color);
    }

    /**
     * 获取颜色的互补色
     * @param color 原颜色
     * @return 互补色
     */
    public static int getComplementaryColor(int color) {
        return Color.argb(
                Color.alpha(color),
                255 - Color.red(color),
                255 - Color.green(color),
                255 - Color.blue(color)
        );
    }

    /**
     * 生成随机颜色
     * @return 随机颜色值
     */
    public static int randomColor() {
        java.util.Random random = new java.util.Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    /**
     * 生成随机颜色（带透明度）
     * @param alpha 透明度 (0-255)
     * @return 随机颜色值
     */
    public static int randomColor(int alpha) {
        java.util.Random random = new java.util.Random();
        return Color.argb(alpha, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
