package com.dawn.library;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 应用工具类
 */
@SuppressWarnings("unused")
public class LAppUtil {

    /**
     * 获取版本名称
     * @param context 上下文
     *
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     * @param context 上下文
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用大小
     * @param context 上下文
     * @param packageName 包名
     *
     * @return 应用大小（字节），失败返回0
     */
    public static long getAppSize(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return new File(applicationInfo.sourceDir).length();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装应用（适配Android 7.0+）
     * @param context 上下文
     * @param filePath 安装包路径
     * @param authority FileProvider的authority
     *
     * @return 是否开始安装
     */
    public static boolean installApk(Context context, String filePath, String authority) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
        return true;
    }

    /**
     * 卸载应用
     * @param context 上下文
     * @param packageName 包名
     *
     * @return 是否开始卸载
     */
    public static boolean uninstallApk(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * 启动应用
     * @param context 上下文
     * @param packageName 包名
     * @return 是否成功启动
     */
    public static boolean runApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return false;
        context.startActivity(intent);
        return true;
    }

    /**
     * 判断应用是否已安装
     * @param context 上下文
     * @param packageName 包名
     * @return 是否已安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取应用名称
     * @param context 上下文
     * @return 应用名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 清除应用内部缓存
     * @param context 上下文
     */
    public static void cleanCache(Context context) {
        LFileUtil.deleteFileByDirectory(context.getCacheDir());
    }

    /**
     * 清除应用内部数据库
     * @param context 上下文
     */
    public static void cleanDatabases(Context context) {
        String filepath = context.getFilesDir().getParent() + File.separator + "databases";
        LFileUtil.deleteFileByDirectory(new File(filepath));
    }

    /**
     * 清除应用内部sp
     * @param context 上下文
     */
    public static void cleanSharedPreference(Context context) {
        String filepath = context.getFilesDir().getParent() + File.separator + "shared_prefs";
        LFileUtil.deleteFileByDirectory(new File(filepath));
    }

    /**
     * 判断是否为Debug模式
     * @param context 上下文
     * @return 是否为Debug模式
     */
    public static boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取包名
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取应用首次安装时间
     * @param context 上下文
     * @return 首次安装时间戳
     */
    public static long getFirstInstallTime(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 获取应用最后更新时间
     * @param context 上下文
     * @return 最后更新时间戳
     */
    public static long getLastUpdateTime(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 获取应用签名的SHA1值
     * @param context 上下文
     * @return SHA1签名字符串
     */
    @SuppressWarnings("deprecation")
    public static String getSignatureSHA1(Context context) {
        try {
            byte[] signatureBytes;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo pi = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                if (pi.signingInfo == null) return "";
                signatureBytes = pi.signingInfo.getApkContentsSigners()[0].toByteArray();
            } else {
                PackageInfo pi = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), PackageManager.GET_SIGNATURES);
                if (pi.signatures == null || pi.signatures.length == 0) return "";
                signatureBytes = pi.signatures[0].toByteArray();
            }
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            md.update(signatureBytes);
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X:", b));
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 打开应用商店中该应用的页面
     * @param context 上下文
     */
    public static void openAppStore(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // 没有应用商店时打开网页版
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 打开应用设置页面
     * @param context 上下文
     */
    public static void openAppSettings(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取应用已申请的权限列表
     * @param context 上下文
     * @return 权限数组
     */
    public static String[] getAppPermissions(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_PERMISSIONS);
            return pi.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            return new String[0];
        }
    }

    /**
     * 获取应用的目标SDK版本
     * @param context 上下文
     * @return 目标SDK版本
     */
    public static int getTargetSdkVersion(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            return info.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 获取指定应用的版本号
     * @param context 上下文
     * @param packageName 应用包名
     * @return 应用的版本号，未找到返回-1
     */
    public static int getVersionCodeOfApp(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * 判断应用是否正在运行
     * @param context 上下文
     * @param packageName 应用包名
     * @return 是否正在运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        android.app.ActivityManager am = (android.app.ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            java.util.List<android.app.ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            if (list != null) {
                for (android.app.ActivityManager.RunningAppProcessInfo info : list) {
                    if (info.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
