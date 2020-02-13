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

    //获取时间段内可用优惠券
    public static RequestBody getAvailableTicketNum() {
        Map<String, Object> params = new HashMap<>();
        return getRequsetBodyInfo(params);
    }

    //获取时间段内可用优惠券
    public static RequestBody getAvailableTicketList(int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return getRequsetBodyInfo(params);
    }

    //我的优惠券
    public static RequestBody getMyTicket(int type, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return getRequsetBodyInfo(params);
    }

    //我的优惠券2
    public static RequestBody getNewTicketList(String sendBeginTime) {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(sendBeginTime)) {
            params.put("sendBeginTime", sendBeginTime);
        }
        return getRequsetBodyInfo(params);
    }

    //验证发送优惠券验证码
    public static RequestBody getVerifyCouponSms(String actticketid,String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("actticketid", actticketid);
        params.put("code", code);
        return getRequsetBodyInfo(params);
    }

    //优惠券消费
    public static RequestBody getCouponConsume(List<String> actticketids, String billnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("actticketidList", actticketids);
        params.put("billnum", billnum);
        return getRequsetBodyInfo(params);
    }

    //发送优惠券验证码
    public static RequestBody getSendCouponSms(String actticketid) {
        Map<String, Object> params = new HashMap<>();
        params.put("actticketid", actticketid);
        return getRequsetBodyInfo(params);
    }

    //获取红包
    public static RequestBody getHbAll(int flag) {
        Map<String,Object> params = new HashMap<>();
        params.put("pageIndex",1);
        params.put("pageSize",500);
        //flag == 0 为 0，1
        List<Integer> mInfos = new ArrayList<>();
        if (flag == 1) {
            // 1 激活成功
            mInfos.add(1);
        }
        params.put("status",mInfos);
        return getRequsetBodyInfo(params);
    }

    //查询红包明细
    public static RequestBody getHbMx(int actionType,int flag) {
        Map<String,Object> params = new HashMap<>();
        params.put("pageIndex",1);
        params.put("pageSize",500);
        params.put("actiontype",actionType);
        //flag == 0 为 1,2或者不传 类型:1收入 2支出
        List<Integer> mInfos = new ArrayList<>();
        if (flag != 0) {
            mInfos.add(flag);
        }
        params.put("type",mInfos);
        return getRequsetBodyInfo(params);
    }

    //使用话费卷
    public static RequestBody shiyongHfj(List<String> mInfos) {
        Map<String,Object> params = new HashMap<>();
        params.put("ticketIds",mInfos);
        return getRequsetBodyInfo(params);
    }

    //兑换卷
    public static RequestBody duihuanJuan(int telTicketNum,int ticketNum) {
        Map<String,Object> params = new HashMap<>();
        params.put("telTicketNum",telTicketNum);
        params.put("ticketNum",ticketNum);
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
