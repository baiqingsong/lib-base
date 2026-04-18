package com.dawn.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 网络工具类
 */
@SuppressWarnings("unused")
public class LNetUtil {
    private static final String NETWORK_TYPE_WIFI = "wifi";
    private static final String NETWORK_TYPE_UNKNOWN = "unknown";
    private static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    /**
     * 获取网络类型
     * @param context 上下文
     *
     * @return int 网络类型
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * 网络是否可用
     * @param context 上下文
     *
     * @return boolean 是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                return info != null && info.isAvailable();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 是否是WiFi
     * @param context 上下文
     *
     * @return boolean 是否是WiFi
     */
    public static boolean isWiFi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info == null)
            return false;
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     * @param activity Activity
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 设置WiFi状态
     * @param context 上下文
     * @param enabled 是否打开WIFI
     */
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null)
            return;
        wifiManager.setWifiEnabled(enabled);
    }

    /**
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @param host 需要连接的外网
     * @param pingCount ping的次数
     * @param stringBuffer ping的结果
     */
    public static boolean ping(String host, int pingCount, StringBuffer stringBuffer) {
        // 防止命令注入：只允许字母、数字、点、冠号和连字符
        if (host == null || !host.matches("^[a-zA-Z0-9.:\\-]+$")) {
            append(stringBuffer, "ping fail: invalid host.");
            return false;
        }
        if (pingCount <= 0 || pingCount > 10) {
            pingCount = 1;
        }
        String line;
        Process process = null;
        BufferedReader successReader = null;
        String command = "ping -c " + pingCount + " " + host;
        boolean isSuccess = false;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                append(stringBuffer, "ping fail:process is null.");
                return false;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                append(stringBuffer, line);
            }
            int status = process.waitFor();
            if (status == 0) {
                append(stringBuffer, "exec cmd success:" + command);
                isSuccess = true;
            } else {
                append(stringBuffer, "exec cmd fail.");
                isSuccess = false;
            }
            append(stringBuffer, "exec finished.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text);
            stringBuffer.append("\n");
        }
    }

    /**
     * 获取网络类型名称
     * @param context 上下文
     * @return 网络类型名称
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return NETWORK_TYPE_UNKNOWN;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return NETWORK_TYPE_DISCONNECT;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_TYPE_WIFI;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return info.getSubtypeName();
        }
        return NETWORK_TYPE_UNKNOWN;
    }

    /**
     * 是否是移动网络
     * @param context 上下文
     * @return 是否是移动网络
     */
    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 获取WiFi信号强度
     * @param context 上下文
     * @return WiFi信号强度
     */
    public static int getWifiSignalStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return 0;
        }
        android.net.wifi.WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return 0;
        }
        return wifiInfo.getRssi();
    }

    /**
     * 获取当前连接的WiFi名称
     * @param context 上下文
     * @return WiFi名称
     */
    public static String getCurrentWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return "";
        }
        android.net.wifi.WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return "";
        }
        String ssid = wifiInfo.getSSID();
        if (ssid != null && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid != null ? ssid : "";
    }

    /**
     * 获取本机IP地址
     * @param context 上下文
     * @return IP地址
     */
    public static String getLocalIPAddress(Context context) {
        try {
            java.util.Enumeration<java.net.NetworkInterface> en = java.net.NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                java.net.NetworkInterface intf = en.nextElement();
                java.util.Enumeration<java.net.InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    java.net.InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 简单的ping测试（只测试一次）
     * @param host 主机地址
     * @return 是否ping通
     */
    public static boolean simplePing(String host) {
        return ping(host, 1, null);
    }

    /**
     * 检查指定端口是否可连接
     * @param host 主机地址
     * @param port 端口号
     * @param timeout 超时时间（毫秒）
     * @return 是否可连接
     */
    public static boolean isPortReachable(String host, int port, int timeout) {
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket();
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket != null) {
                try { socket.close(); } catch (Exception ignored) {}
            }
        }
    }

    /**
     * 获取本机MAC地址
     * @return MAC地址
     */
    public static String getMacAddress() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> interfaces =
                    java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface networkInterface = interfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null || mac.length == 0) continue;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X", mac[i]));
                    if (i < mac.length - 1) sb.append(":");
                }
                String macStr = sb.toString();
                // 跳过全0的地址
                if (!"00:00:00:00:00:00".equals(macStr)) {
                    return macStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * URL编码
     * @param url 原始URL
     * @return 编码后的URL
     */
    public static String encodeUrl(String url) {
        try {
            return java.net.URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * URL解码
     * @param url 编码后的URL
     * @return 解码后的URL
     */
    public static String decodeUrl(String url) {
        try {
            return java.net.URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * 判断当前网络是否为以太网
     * @param context 上下文
     * @return 是否为以太网
     */
    public static boolean isEthernet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_ETHERNET;
    }

    /**
     * 从 ping 输出中解析平均耗时（ms）。如果失败返回 null。
     * @param host 主机地址
     * @param pingCount ping次数
     * @return 平均延迟毫秒数，失败返回null
     */
    public static Double getPingAverageMs(String host, int pingCount) {
        StringBuffer sb = new StringBuffer();
        boolean ok = ping(host, pingCount, sb);
        if (!ok) return null;
        String out = sb.toString();
        for (String line : out.split("\n")) {
            if (line.contains("avg")) {
                int eq = line.indexOf('=');
                if (eq >= 0 && eq + 1 < line.length()) {
                    String tail = line.substring(eq + 1).trim().replace(" ms", "").trim();
                    String[] parts = tail.split("/");
                    if (parts.length >= 2) {
                        try {
                            return Double.parseDouble(parts[1]);
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 网络是否已连接
     * @param context 上下文
     * @return 是否已连接
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isAvailable();
    }

}
