package com.uidt.qmrz_zy.mvp.contract;

import com.uidt.qmrz_zy.base.BaseModel;
import com.uidt.qmrz_zy.base.BasePresenter;
import com.uidt.qmrz_zy.base.BaseView;
import com.uidt.qmrz_zy.bean.QmkyLevelBean;

import rx.Observable;

/**
 * @author yijixin on 2020-02-13
 */
public interface SureInfoContract {
    interface Model extends BaseModel {
        Observable<QmkyLevelBean> getPassResult(String personalid,String addr,String name,int gender,String birthdate,String nationality);
    }

    interface View extends BaseView {
        void loadPassResult(QmkyLevelBean qmkyLevelBean);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void passlevelInfos(String personalid,String addr,String name,int gender,String birthdate,String nationality);
    }
}
