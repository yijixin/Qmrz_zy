package com.uidt.qmrz_zy;

import android.os.Bundle;
import android.view.View;

import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.mvp.contract.CommonContract;
import com.uidt.qmrz_zy.mvp.model.CommonModel;
import com.uidt.qmrz_zy.mvp.presenter.CommonPresenter;

public class ResultActivity extends BaseActivity<CommonPresenter, CommonModel> implements CommonContract.View, View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
