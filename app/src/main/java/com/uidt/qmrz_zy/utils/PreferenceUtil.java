package com.uidt.qmrz_zy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.uidt.qmrz_zy.app.AppAplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yijixin at 2017/8/4
 */
public class PreferenceUtil {

    public static boolean getBoolean(Context context, String key, Boolean defValue) {
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getBoolean(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getString(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getInt(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //存Object对象
    public static void saveObjectInfo(Context context,  String key, Object object) throws Exception {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);//把对象写到流里
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, temp);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于保存集合
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean putHashMapData(Map<K, V> map, String name) {
        boolean result;
        SharedPreferences sp = AppAplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString("recognizeBleMap", json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于取出集合
     *
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(String name) {
        SharedPreferences sp = AppAplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        String json = sp.getString("recognizeBleMap", "");
        Gson gson = new Gson();
        HashMap hashMap = gson.fromJson(json, HashMap.class);
        return hashMap;
    }
}
