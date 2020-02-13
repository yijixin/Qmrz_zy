package com.uidt.qmrz_zy.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ApiGetInterfaceParams {

    public static RequestBody getPassLevel(String personalid,String addr,String name,int gender,String birthdate,String nationality) {
        Map<String,Object> params = new HashMap<>();
        params.put("personalid",personalid);
        params.put("addr",addr);
        params.put("name",name);
        params.put("gender",gender);
        params.put("birthdate",birthdate);
        params.put("nationality",nationality);
        return getRequsetBodyInfo(params);
    }

    private static RequestBody getRequsetBodyInfo(Map<String, Object> params) {
        Gson gson = new Gson();
        String s = gson.toJson(params);
        return RequestBody.create(MediaType.parse("application/json"), s);
    }

    public static String getJsonInfos(ResponseBody responseBody) {
        String jsonInfos = "";
        if (responseBody == null) {
            return jsonInfos;
        }
        try {
            String json = responseBody.string();
            jsonInfos = json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInfos;
    }

    public static int getState(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getMessageError(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "网络异常";
    }
}
