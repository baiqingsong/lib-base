package com.dawn.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * 系统工具类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LSystemUtil {

    /**
     * 重启手机
     */
    public static void reboot(){
        try {
            Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开系统的设置页面
     */
    public static void openSettings(Context context){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 安装apk
     * @param context 上下文
     * @param apkfile apk文件
     * @param authority FileProvider的authority
     */
    public static void installApk(Context context, File apkfile, String authority) {
        if (apkfile == null || !apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authority, apkfile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkfile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager == null)
            return;
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     */
    public static void hideSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager == null)
            return;
        if (!(context instanceof Activity))
            return;
        View view = ((Activity) context).getCurrentFocus();
        if(view == null)
            return;
        IBinder iBinder = view.getWindowToken();
        if(iBinder == null)
            return;
        inputMethodManager.hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取屏幕宽度
     * @param context 上下文
     * @return 屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context 上下文
     * @return 屏幕高度（像素）
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕密度
     * @param context 上下文
     * @return 屏幕密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * dp转px
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * @param context 上下文
     * @return 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 复制文本到剪贴板
     * @param context 上下文
     * @param text 要复制的文本
     */
    public static void copyToClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 从剪贴板获取文本
     * @param context 上下文
     * @return 剪贴板文本
     */
    public static String getFromClipboard(Context context) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            android.content.ClipData clipData = clipboard.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                android.content.ClipData.Item item = clipData.getItemAt(0);
                CharSequence text = item.getText();
                return text != null ? text.toString() : "";
            }
        }
        return "";
    }

    /**
     * 打开拨号界面
     * @param context 上下文
     * @param phoneNumber 电话号码
     */
    public static void openDialer(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 发送短信
     * @param context 上下文
     * @param phoneNumber 电话号码
     * @param message 短信内容
     */
    public static void sendSMS(Context context, String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }

    /**
     * 打开浏览器
     * @param context 上下文
     * @param url 网址
     */
    public static void openBrowser(Context context, String url) {
        if (url == null || url.isEmpty()) return;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送邮件
     * @param context 上下文
     * @param email 收件人邮箱
     * @param subject 主题
     * @param body 内容
     */
    public static void sendEmail(Context context, String email, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 分享文字
     * @param context 上下文
     * @param text 分享的文本
     * @param title 分享对话框标题
     */
    public static void shareText(Context context, String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 分享图片
     * @param context 上下文
     * @param imageUri 图片Uri
     * @param title 分享对话框标题
     */
    public static void shareImage(Context context, Uri imageUri, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 判断是否为平板设备
     * @param context 上下文
     * @return 是否为平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)
                >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断是否为横屏
     * @param context 上下文
     * @return 是否为横屏
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否为竖屏
     * @param context 上下文
     * @return 是否为竖屏
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 打开系统WiFi设置页面
     * @param context 上下文
     */
    public static void openWifiSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统蓝牙设置页面
     * @param context 上下文
     */
    public static void openBluetoothSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统显示设置页面
     * @param context 上下文
     */
    public static void openDisplaySettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取应用可用的最大堆内存（MB）
     * @param context 上下文
     * @return 最大堆内存（MB）
     */
    public static int getMaxHeapMemory(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        return am != null ? am.getMemoryClass() : 0;
    }
}
