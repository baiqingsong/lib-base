package com.dawn.library;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * SharedPreferences工具类
 */
@SuppressWarnings("unused")
public class LSPUtil {
    /**
     * 存储SP值
     * @param context 上下文
     * @param key 键
     * @param object 值
     */
    public static void setSP(Context context, String key, Object object) {
        if (object == null) return;
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        putValue(sp, key, object);
    }

    /**
     * 获取SP值
     * @param context 上下文
     * @param key 键
     * @param defaultObject 默认值
     */
    public static Object getSP(Context context, String key, Object defaultObject) {
        if (defaultObject == null) return null;
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return getValue(sp, key, defaultObject);
    }

    /**
     * 清除所有的SP值
     */
    public static void cleanAllSP(Context context) {
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    /**
     * 清除指定key的SP值
     * @param context 上下文
     * @param key 键
     */
    public static void removeSP(Context context, String key) {
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    /**
     * 检查是否包含指定key
     * @param context 上下文
     * @param key 键
     * @return 是否包含
     */
    public static boolean containsKey(Context context, String key) {
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 获取所有的key
     * @param context 上下文
     * @return 所有key的集合
     */
    public static Set<String> getAllKeys(Context context) {
        String packageName = context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return sp.getAll().keySet();
    }

    /**
     * 使用自定义名称的SharedPreferences存储
     * @param context 上下文
     * @param spName SP文件名
     * @param key 键
     * @param object 值
     */
    public static void setSP(Context context, String spName, String key, Object object) {
        if (object == null) return;
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        putValue(sp, key, object);
    }

    /**
     * 使用自定义名称的SharedPreferences获取
     * @param context 上下文
     * @param spName SP文件名
     * @param key 键
     * @param defaultObject 默认值
     * @return 获取的值
     */
    public static Object getSP(Context context, String spName, String key, Object defaultObject) {
        if (defaultObject == null) return null;
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return getValue(sp, key, defaultObject);
    }

    @SuppressWarnings("unchecked")
    private static void putValue(SharedPreferences sp, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor edit = sp.edit();
        if ("String".equals(type)) {
            edit.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            edit.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            edit.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            edit.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            edit.putLong(key, (Long) object);
        } else if (object instanceof Set) {
            edit.putStringSet(key, (Set<String>) object);
        }
        edit.apply();
    }

    @SuppressWarnings("unchecked")
    private static Object getValue(SharedPreferences sp, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defaultObject);
        }
        return null;
    }
}
