package com.dawn.library;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast工具类
 * 避免重复显示Toast，支持在子线程中调用
 */
@SuppressWarnings("unused")
public class LToastUtil {
    private static Toast sToast;
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 短时间显示Toast
     * @param context 上下文
     * @param message 显示内容
     */
    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     * @param context 上下文
     * @param resId 字符串资源ID
     */
    public static void showShort(Context context, int resId) {
        show(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     * @param context 上下文
     * @param message 显示内容
     */
    public static void showLong(Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     * @param context 上下文
     * @param resId 字符串资源ID
     */
    public static void showLong(Context context, int resId) {
        show(context, context.getString(resId), Toast.LENGTH_LONG);
    }

    /**
     * 显示Toast（自动取消上一个）
     * @param context 上下文
     * @param message 显示内容
     * @param duration 显示时长
     */
    private static void show(final Context context, final String message, final int duration) {
        if (context == null || message == null) return;
        final Context appContext = context.getApplicationContext();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showInternal(appContext, message, duration);
        } else {
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    showInternal(appContext, message, duration);
                }
            });
        }
    }

    private static void showInternal(Context context, String message, int duration) {
        cancel();
        sToast = Toast.makeText(context, message, duration);
        sToast.show();
    }

    /**
     * 取消当前显示的Toast
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
