package com.uidt.qmrz_zy.mvp.contract;

import com.uidt.qmrz_zy.base.BaseModel;
import com.uidt.qmrz_zy.base.BasePresenter;
import com.uidt.qmrz_zy.base.BaseView;

/**
 * @author yijixin on 2020-02-12
 */
public interface CommonContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<View,Model> {
    }
}
