package com.uidt.qmrz_zy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.mvp.contract.CommonContract;
import com.uidt.qmrz_zy.mvp.model.CommonModel;
import com.uidt.qmrz_zy.mvp.presenter.CommonPresenter;

public class SureInfoActivity extends BaseActivity<CommonPresenter, CommonModel> implements CommonContract.View, View.OnClickListener {

    private Button btnCommit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sure_info;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        btnCommit = findViewById(R.id.btn_commit_tj);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit_tj:
                break;
            default:
        }
    }
}
