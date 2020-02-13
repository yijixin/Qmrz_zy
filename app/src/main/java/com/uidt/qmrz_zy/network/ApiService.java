package com.uidt.qmrz_zy.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Created by yijixin at 2017/11/8
 * @author yijixin
 */
public interface ApiService {

    //兑换卷
    @POST("pass/personal/getpasslevel")
    Call<ResponseBody> getpasslevel(@Body RequestBody requestBody);
}
