package com.uidt.qmrz_zy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.mvp.contract.CommonContract;
import com.uidt.qmrz_zy.mvp.model.CommonModel;
import com.uidt.qmrz_zy.mvp.presenter.CommonPresenter;

public class ResultActivity extends BaseActivity<CommonPresenter, CommonModel> implements CommonContract.View, View.OnClickListener {

    private Button btnComplete;

    public static void startAction(Activity activity, boolean result) {
        Intent intent = new Intent(activity, ResultActivity.class);
        intent.putExtra("result", result);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ImageView ivResult = findViewById(R.id.iv_hy_tbicon);
        TextView tvResultOne = findViewById(R.id.tv_hytg_result);
        TextView tvResultTwo = findViewById(R.id.tv_yxfx_result);
        btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);

        boolean result = getIntent().getBooleanExtra("result", false);
        if (result) {
            //成功
            tvResultTwo.setText("允许放行");
            tvResultOne.setText("核验通过！");
            tvResultOne.setTextColor(getResources().getColor(R.color.color_1E78EB));
            ivResult.setImageResource(R.mipmap.icon_rz_success);
        } else {
            //失败
            tvResultTwo.setText("不允许放行");
            tvResultOne.setText("核验未通过！");
            tvResultOne.setTextColor(getResources().getColor(R.color.color_a10111));
            ivResult.setImageResource(R.mipmap.icon_rz_fail);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete:
                finish();
                break;
            default:
        }
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
