package com.uidt.qmrz_zy.mvp.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.uidt.qmrz_zy.base.RxSchedulers;
import com.uidt.qmrz_zy.bean.QmkyLevelBean;
import com.uidt.qmrz_zy.mvp.contract.SureInfoContract;
import com.uidt.qmrz_zy.network.ApiGetInterfaceParams;
import com.uidt.qmrz_zy.network.ApiService;
import com.uidt.qmrz_zy.network.ServiceGenerator;
import com.uidt.qmrz_zy.utils.InterfaceResultUtils;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @author yijixin on 2020-02-13
 */
public class SureInfoModel implements SureInfoContract.Model {
    @Override
    public Observable<QmkyLevelBean> getPassResult(final String personalid, final String addr, final String name, final int gender, final String birthdate, final String nationality) {
        return Observable.create(new Observable.OnSubscribe<QmkyLevelBean>() {
            @Override
            public void call(final Subscriber<? super QmkyLevelBean> subscriber) {
                ApiService apiService = ServiceGenerator.createServiceCoupon(ApiService.class);
                RequestBody availableTicketNum = ApiGetInterfaceParams.getPassLevel(personalid, addr, name, gender, birthdate, nationality);
                Call<ResponseBody> responseBodyCall = apiService.getpasslevel(availableTicketNum);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            ResponseBody body = response.body();
                            String jsonInfos = ApiGetInterfaceParams.getJsonInfos(body);
                            if (!TextUtils.isEmpty(jsonInfos)) {
                                if (ApiGetInterfaceParams.getState(jsonInfos) == InterfaceResultUtils.NET_RESULT_OK) {
                                    Gson gson = new Gson();
                                    QmkyLevelBean qmkyLevelBean = gson.fromJson(jsonInfos,QmkyLevelBean.class);
                                    subscriber.onNext(qmkyLevelBean);
                                } else {
                                    String messageError = ApiGetInterfaceParams.getMessageError(jsonInfos);
                                    int state = ApiGetInterfaceParams.getState(jsonInfos);
                                    QmkyLevelBean qmkyLevelBean = new QmkyLevelBean();
                                    qmkyLevelBean.setStatus(state);
                                    qmkyLevelBean.setMsg(messageError);
                                    subscriber.onNext(qmkyLevelBean);
                                }
                            } else {
                                QmkyLevelBean newHistoryBean = new QmkyLevelBean();
                                newHistoryBean.setStatus(444);
                                newHistoryBean.setMsg("网络异常");
                                subscriber.onNext(newHistoryBean);
                            }
                        } else {
                            QmkyLevelBean newHistoryBean = new QmkyLevelBean();
                            newHistoryBean.setStatus(444);
                            newHistoryBean.setMsg("网络异常"+response.code());
                            subscriber.onNext(newHistoryBean);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        QmkyLevelBean newHistoryBean = new QmkyLevelBean();
                        newHistoryBean.setStatus(444);
                        newHistoryBean.setMsg("网络异常");
                        subscriber.onNext(newHistoryBean);
                    }
                });
            }
        }).compose(RxSchedulers.<QmkyLevelBean>io_main());
    }
}
