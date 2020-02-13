package com.uidt.qmrz_zy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.mvp.contract.CommonContract;
import com.uidt.qmrz_zy.mvp.model.CommonModel;
import com.uidt.qmrz_zy.mvp.presenter.CommonPresenter;
import com.uidt.qmrz_zy.utils.DialogUtils;

public class SureInfoActivity extends BaseActivity<CommonPresenter, CommonModel> implements CommonContract.View, View.OnClickListener {

    private Button btnCommit;
    private LinearLayout llBack;
    private EditText etSfzh,etAddress;

    public static void startAction(Activity activity,String cardnum,String address) {
        Intent intent = new Intent(activity,SureInfoActivity.class);
        intent.putExtra("cardnum",cardnum);
        intent.putExtra("address",address);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sure_info;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        btnCommit = findViewById(R.id.btn_commit_tj);
        btnCommit.setOnClickListener(this);
        etSfzh = findViewById(R.id.et_sfzhm);
        etAddress = findViewById(R.id.et_address);
        llBack = findViewById(R.id.ll_back_hzinfo);
        llBack.setOnClickListener(this);

        String cardNum = getIntent().getStringExtra("cardnum");
        String address = getIntent().getStringExtra("address");
        etSfzh.setText(cardNum);
        etAddress.setText(address);
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
                String idNumber = etSfzh.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if (TextUtils.isEmpty(idNumber) && TextUtils.isEmpty(address)) {
                    Toast.makeText(mContext, "身份证信息和地址信息不能为空！", Toast.LENGTH_SHORT).show();
                } else {
//                    ResultActivity.startAction(SureInfoActivity.this,true);
                    ResultActivity.startAction(SureInfoActivity.this,false);
                    finish();

//                    DialogUtils.getDialogUtils(SureInfoActivity.this).show();
                    //接口调用判断
                }
                break;
            case R.id.ll_back_hzinfo:
                finish();
                break;
            default:
        }
    }
}
