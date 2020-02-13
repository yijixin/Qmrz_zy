package com.uidt.qmrz_zy.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author Created by yijixin at 2017/11/8
 * @author yijixin
 */
public interface ApiService {

    //查询订单信息 servicetype 不传是全部查询 1是普通钥匙账单 2是蓝牙扣账单
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryBillRecord.do")
    Call<ResponseBody> queryBillRecord(@Field("keyholder") String keyholder, @Field("billstate") String billstate, @Field("servicetype") String servicetype);

    //app用户使用协议 0 房客 1服务费 2代理人 3房东
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!agreeProtocol.do")
    Call<ResponseBody> agreeProtocolInfos(@Field("account") String account, @Field("type") int type, @Field("isagree") int isagree);

    //app请求历史消息列表
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryHistoryMessage.do")
    Call<ResponseBody> queryHistoryMessage(@Field("msgreceiver") String msgreceiver, @Field("start") int start, @Field("limit") int limit);

    //支付请求获取订单信息
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/aliPayAction!receiveOrder.do")
    Call<ResponseBody> receiveOrderPay(@Field("account") String account, @Field("billid") String billid, @Field("total_fee") String total_fee, @Field("subject") String subject, @Field("body") String body, @Field("paymethod") String paymethod, @Field("paymonths") String paymonths);

    //支付请求获取订单信息（优惠券）
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/aliPayAction!receiveOrderActTick.do")
    Call<ResponseBody> receiveOrderActTick(@Field("account") String account, @Field("billid") String billid, @Field("total_fee") String total_fee, @Field("subject") String subject, @Field("body") String body, @Field("paymethod") String paymethod, @Field("paymonths") String paymonths, @Field("deductionmoney") String deductionmoney, @Field("actuseridlist") String actuseridlist);

    //支付成功回执
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/aliPayAction!receiveNotify_App.do")
    Call<ResponseBody> receiveNotifyApp(@Field("billid") String billid, @Field("appaccount") String appaccount, @Field("paymethod") String paymethod, @Field("sign") String sign);

    //商城开关查询 是否显示  新版App废弃
//    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryMallSwitch.do")
    Call<ResponseBody> queryMallSwitch();

    /**
     * 上传国外认证结果
     * @param idtype 证件类型 0：身份证 1：港澳通行证 2：护照 3：其它
     * @param state 认证结果 0未认证，1认证成功，2认证失败  统一填2
     * @param idnum 证件号
     * @param name 姓名
     * @param useraccount 用户账号
     * @param picture 人脸
     * @param frontidphoto 	证件照正面
     * @param backidphoto 	证件照反面
     * @param holdidphoto 手持证件照
     * @return
     */
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!upAuthResult_foreigner.do")
    Call<ResponseBody> upAuthResultForen(@Field("idtype") String idtype, @Field("state") String state, @Field("idnum") String idnum, @Field("name") String name, @Field("useraccount") String useraccount, @Field("picture") String picture, @Field("frontidphoto") String frontidphoto, @Field("backidphoto") String backidphoto, @Field("holdidphoto") String holdidphoto, @Field("workplace") String workplace);

    //商城开关查询 是否显示  新版
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryMallChoiceSwitch.do")
    Call<ResponseBody> queryMallChoiceSwitch();

    //获取ip
    @FormUrlEncoded
    @POST("/alpub/lock/AppinterfaceAction/appinterfaceAction!nbRegUrl.do")
    Call<ResponseBody> upNbRegUrl(@Field("imsi") String imsi, @Field("imei") String imei);

    //查询公告内容
//    @FormUrlEncoded
    @POST("/alpub/lock/AppinterfaceAction/appinterfaceAction!publishmessage.do")
    Call<ResponseBody> publishmessage();

    //紫光锁向后台更新密钥
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!uploadLockData.do")
    Call<ResponseBody> uploadLockData(@Field("account") String account, @Field("keyid") String keyid, @Field("data") String data);

    //刷脸登录
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!loginApp_face.do")
    Call<ResponseBody> loginAppFace(@Header("appinfo") String appinfo, @Field("account") String account, @Field("facebase64") String facebase64);

    //视频地址下载
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!obtainplayvideoadurl.do")
    Call<ResponseBody> getplayvideoadurl(@Field("lockid") String lockid);

    //获取轮播图
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryAdpicloopCfg.do")
    Call<ResponseBody> queryAdoucloopCfg(@Field("lockid") String lockid);

    //确认同住人
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!confirmCoResidentsQueryOrUpd.do")
    Call<ResponseBody> confirmCoresidentsqueryOrUpd(@Field("type") String type, @Field("lockid") String lockid, @Field("account") String account, @Field("recordid") String recordid, @Field("confirmstate") String confirmstate);

    //查询其他人的冻结钥匙
    @FormUrlEncoded
    @POST("/alpub/lock/interfaceforapp/appinterfaceAction!queryFrozenKey.do")
    Call<ResponseBody> queryFrozenKey(@Field("useraccount") String useraccount, @Field("lockid") String lockid, @Field("keyid") String keyid);

    //获取优惠券数量
//    @Headers("Cookie:JSESSIONID=8D7F812DC3AB6C83C0780F65A72EC42A")
    @POST("app/actticket/availableTicketNum")
    Call<ResponseBody> availableTicketNum(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //获取时间段内可用优惠券
//    @Headers("Cookie:JSESSIONID=8D7F812DC3AB6C83C0780F65A72EC42A")
    @POST("app/actticket/availableTicketList")
    Call<ResponseBody> availableTicketList(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //我的优惠券
//    @Headers("Cookie:JSESSIONID=8D7F812DC3AB6C83C0780F65A72EC42A")
    @POST("app/actticket/myTicket")
    Call<ResponseBody> myTicket(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //新优惠券提醒1
//    @Headers("Cookie:JSESSIONID=8D7F812DC3AB6C83C0780F65A72EC42A")
    @POST("app/actticket/newTicket")
    Call<ResponseBody> newTicket(@Header("Cookie") String sessionId);

    //新优惠券提醒2
//    @Headers("Cookie:JSESSIONID=8D7F812DC3AB6C83C0780F65A72EC42A")
    @POST("app/actticket/newTicketList")
    Call<ResponseBody> newTicketList(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //优惠券发送短信
    @POST("app/actsms/send")
    Call<ResponseBody> sendSmsCode(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //验证验证码
    @POST("app/actsms/verify")
    Call<ResponseBody> verifySmsCode(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //消费优惠券
    @POST("app/actticket/consume")
    Call<ResponseBody> consumeCoupon(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //提醒新红包
    @POST("app/myredpacket/txnewnum")
    Call<ResponseBody> txNewHbNum(@Header("Cookie") String sessionId);

    //获取我的红包数
    @POST("app/myredpacket/redpacketnum")
    Call<ResponseBody> redpacketHbnum(@Header("Cookie") String sessionId);

    //提醒红包总数据
    @POST("app/myredpacket/txnum")
    Call<ResponseBody> txnumHb(@Header("Cookie") String sessionId);

    //设置已提醒
    @POST("app/myredpacket/setremindedall")
    Call<ResponseBody> setremindedall(@Header("Cookie") String sessionId);

    //我的红包
    @POST("app/myredpacket/getall")
    Call<ResponseBody> getall(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //红包历史记录
    @POST("app/myredpacket/history")
    Call<ResponseBody> historyInfo(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //使用话费卷
    @POST("app/actticket/applyRecharge")
    Call<ResponseBody> applyRecharge(@Header("Cookie") String sessionId, @Body RequestBody requestBody);

    //兑换卷
    @POST("app/myredpacket/exchangeticket")
    Call<ResponseBody> exchangeticket(@Header("Cookie") String sessionId, @Body RequestBody requestBody);
}
