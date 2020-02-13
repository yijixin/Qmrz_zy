package com.uidt.qmrz_zy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.bean.QmkyLevelBean;
import com.uidt.qmrz_zy.mvp.contract.SureInfoContract;
import com.uidt.qmrz_zy.mvp.model.SureInfoModel;
import com.uidt.qmrz_zy.mvp.presenter.SureInfoPresenter;
import com.uidt.qmrz_zy.utils.DialogUtils;
import com.uidt.qmrz_zy.utils.InterfaceResultUtils;

public class SureInfoActivity extends BaseActivity<SureInfoPresenter, SureInfoModel> implements SureInfoContract.View, View.OnClickListener {

    private Button btnCommit;
    private LinearLayout llBack;
    private EditText etSfzh, etAddress;
    private String name, gender, birthday, ethnic;

    public static void startAction(Activity activity, String cardnum, String address, String name, String gender, String birthday, String ethnic) {
        Intent intent = new Intent(activity, SureInfoActivity.class);
        intent.putExtra("cardnum", cardnum);
        intent.putExtra("address", address);
        intent.putExtra("name", name);
        intent.putExtra("gender", gender);
        intent.putExtra("birthday", birthday);
        intent.putExtra("ethnic", ethnic);
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
        name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        birthday = getIntent().getStringExtra("birthday");
        ethnic = getIntent().getStringExtra("ethnic");

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
                    DialogUtils.getDialogUtils(SureInfoActivity.this).show();
                    //接口调用判断
                    int sex = 0;
                    if ("男".equals(gender)) {
                        sex = 1;
                    } else if ("女".equals(gender)) {
                        sex = 2;
                    }
                    mPresenter.passlevelInfos(idNumber, address, name, sex, birthday, ethnic);
                }
                break;
            case R.id.ll_back_hzinfo:
                finish();
                break;
            default:
        }
    }

    @Override
    public void loadPassResult(QmkyLevelBean qmkyLevelBean) {
        DialogUtils.getDialogUtils(SureInfoActivity.this).dismiss();
        if (qmkyLevelBean.getStatus() == InterfaceResultUtils.NET_RESULT_OK) {
            if (qmkyLevelBean.getData().getPassstatus() == 1) {
                ResultActivity.startAction(SureInfoActivity.this, true);
            } else {
                ResultActivity.startAction(SureInfoActivity.this, false);
            }
            finish();
        } else {
            Toast.makeText(mContext, qmkyLevelBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
