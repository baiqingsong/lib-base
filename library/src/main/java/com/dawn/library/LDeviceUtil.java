package com.dawn.library;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;

import java.io.FileReader;
import java.io.Reader;

public class LDeviceUtil {

    /**
     * 获取主板序列号
     */
    public static String getDeviceId() {
        String deviceId = null;
        try {//有线的mac
            String mac = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().trim();
            if (mac.length() >= 17) {
                deviceId = mac.substring(0, 17).replace(":", "").replace("-", "") + "00000000";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    /**
     * 根据路径获取文件内容
     * @param fileName 文件路径
     * @return 文件内容
     * @throws Exception 读取异常
     */
    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
            String text = loadReaderAsString(reader);
            return text;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * 根据流获取内容
     * @param reader
     * @return
     * @throws Exception
     */
    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 获取CPU序列号
     * @return CPU序列号
     */
    public static String getCpuSerial() {
        try {
            return loadFileAsString("/proc/cpuinfo").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取内存信息
     * @return 内存信息
     */
    public static String getMemInfo() {
        try {
            return loadFileAsString("/proc/meminfo").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取系统版本信息
     * @return 系统版本信息
     */
    public static String getSystemVersion() {
        try {
            return loadFileAsString("/proc/version").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取WIFI MAC地址
     * @return WIFI MAC地址
     */
    public static String getWifiMac() {
        try {
            String mac = loadFileAsString("/sys/class/net/wlan0/address");
            if (mac != null) {
                return mac.toUpperCase().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备制造商
     * @return 设备制造商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * @return 设备型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备品牌
     * @return 设备品牌
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取Android版本号
     * @return Android版本号（如 "13"）
     */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号
     * @return SDK版本号
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备指纹
     * @return 设备指纹
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取设备硬件名称
     * @return 硬件名称
     */
    public static String getHardware() {
        return Build.HARDWARE;
    }

    /**
     * 获取设备显示名称
     * @return 显示名称
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 获取系统语言
     * @return 系统语言
     */
    public static String getLanguage() {
        return java.util.Locale.getDefault().getLanguage();
    }

    /**
     * 获取系统国家/地区
     * @return 国家/地区代码
     */
    public static String getCountry() {
        return java.util.Locale.getDefault().getCountry();
    }

    /**
     * 获取设备可用内存（字节）
     * @param context 上下文
     * @return 可用内存大小
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return 0;
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * 获取设备总内存（字节）
     * @param context 上下文
     * @return 总内存大小
     */
    public static long getTotalMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return 0;
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.totalMem;
    }

    /**
     * 判断设备内存是否不足
     * @param context 上下文
     * @return 是否低内存
     */
    public static boolean isLowMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return false;
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.lowMemory;
    }

    /**
     * 获取内部存储可用空间（字节）
     * @return 可用空间
     */
    public static long getInternalStorageAvailable() {
        StatFs stat = new StatFs(android.os.Environment.getDataDirectory().getPath());
        return stat.getAvailableBytes();
    }

    /**
     * 获取内部存储总空间（字节）
     * @return 总空间
     */
    public static long getInternalStorageTotal() {
        StatFs stat = new StatFs(android.os.Environment.getDataDirectory().getPath());
        return stat.getTotalBytes();
    }

    /**
     * 获取设备综合信息
     * @return 设备信息字符串
     */
    public static String getDeviceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Brand: ").append(getBrand()).append("\n");
        sb.append("Model: ").append(getModel()).append("\n");
        sb.append("Manufacturer: ").append(getManufacturer()).append("\n");
        sb.append("Android: ").append(getAndroidVersion()).append("\n");
        sb.append("SDK: ").append(getSDKVersion()).append("\n");
        sb.append("Hardware: ").append(getHardware()).append("\n");
        sb.append("Language: ").append(getLanguage()).append("\n");
        return sb.toString();
    }

    /**
     * 判断是否为模拟器
     * @return 是否为模拟器
     */
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
