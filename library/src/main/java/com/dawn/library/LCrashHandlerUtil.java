package com.dawn.library;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * 报错信息收集
 * 需要调用初始化
 * init
 * 需要传入回调
 * setListener
 */
@SuppressWarnings("unused")
public class LCrashHandlerUtil implements Thread.UncaughtExceptionHandler {
    private static final String TAG = LCrashHandlerUtil.class.getSimpleName();
    private static final boolean DEBUG = true;
    /**
     * 文件名
     */
    public static final String FILE_NAME = "crash";
    /**
     * 异常日志 存储位置为根目录下的 Crash文件夹
     */
    private static String PATH = null;
    private static final String PATH_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 文件名后缀
     */
    private static final String FILE_NAME_SUFFIX = ".trace";

    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(PATH_DATE_PATTERN, Locale.getDefault());
    }

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private String fileNameException;


    private static LCrashHandlerUtil sInstance = new LCrashHandlerUtil();
    private LCrashHandlerUtil() {}
    public static LCrashHandlerUtil getInstance() {
        return sInstance;
    }

    private OnSetCrashHandlerListener mListener;
    public void setListener(OnSetCrashHandlerListener listener){
        this.mListener = listener;
    }

    /**
     * 初始化
     */
    public LCrashHandlerUtil init(Context context) {
        //得到系统的应用异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前应用异常处理器改为默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();

        PATH = getFilePath(context);
        if (PATH != null) {
            checkFilePath(PATH);
        }
        return sInstance;
    }


    /**
     * 这个是最关键的函数，当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     *
     * @param thread 为出现未捕获异常的线程
     * @param ex     为未捕获的异常 ，可以通过e 拿到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        //导入异常信息到SD卡中
        dumpExceptionToSDCard(ex);
        //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决Bug
        uploadExceptionToServer(ex);
//        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }

    }

    /**
     * 将异常信息写入SD卡
     */
    private void dumpExceptionToSDCard(Throwable e) {
        if (PATH == null) {
            return;
        }
        checkFilePath(PATH);

        Date date = new Date();
        String time = getDateFormat().format(date);
        fileNameException = getFileName(date);

        try{
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileNameException))));
            //写入时间
            pw.println(time);
            //写入手机信息
            dumpPhoneInfo(pw);
            pw.println();//换行
            e.printStackTrace(pw);
            pw.close();//关闭输入流
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 获取手机各项信息
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //得到包管理器
        PackageManager pm = mContext.getPackageManager();
        //得到包对象
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        //写入APP版本号
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        //写入 Android 版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.println(Arrays.toString(Build.SUPPORTED_ABIS));
        }else {
            pw.println(Build.CPU_ABI);
        }
    }

    /**
     * 获得文件存储路径
     */
    @SuppressWarnings("WeakerAccess")
    public String getFilePath(Context context) {
        if(context == null)
            return null;
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir != null && externalDir.exists()) {
            return externalDir.getPath() + "/Crash/";
        }
        return context.getFilesDir().getPath() + "/Crash/";
    }

    /**
     * 获取日志写入文件名称
     * @param date 日期
     */
    @SuppressWarnings("WeakerAccess")
    public String getFileName(Date date){
        return PATH + "crash_" + getDateFormat().format(date) + ".trace";
    }

    /**
     * 检查文件目录，主要是查看是否存在，是否暂用内存过多
     * @param pathName 路径
     */
    private void checkFilePath(String pathName){
        if (pathName == null) return;
        File file = new File(pathName);
        if (file.exists()) {
            if (getFolderSize(file) > 100 * 1024 * 1024) {
                deleteFolder(file);
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 查看文件大小，限制文件小于20M
     * @param fileName 文件地址
     */
    private void checkFileSize(String fileName){
        try{
            File file = new File(fileName);
            if(file.exists() && file.length() > 10 * 1024  * 1024){
                file.delete();
            }
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取文件大小
     * @param file 文件
     */
    @SuppressWarnings("WeakerAccess")
    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) return 0;
            for(File fileSingle: fileList){
                if(fileSingle.isDirectory()){
                    size +=  getFolderSize(fileSingle);
                }else{
                    size += fileSingle.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除日志
     * @param file 需要删除的文件
     */
    @SuppressWarnings("WeakerAccess")
    public boolean deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return true;
            boolean deleteAll = true;
            for(File fileSingle: files){
                if(!deleteFolder(fileSingle)){
                    deleteAll = false;
                }
            }
            return deleteAll;
        } else if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 将错误信息上传至服务器
     */
    private void uploadExceptionToServer(Throwable ex) {
        if(mListener != null)
            mListener.exceptionHandling(mContext, ex, fileNameException);
    }
    public interface OnSetCrashHandlerListener{
        void exceptionHandling(Context context, Throwable e, String fileName);
    }
}
