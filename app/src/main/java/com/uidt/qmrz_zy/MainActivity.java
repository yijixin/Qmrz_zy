package com.uidt.qmrz_zy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.uidt.qmrz_zy.base.BaseActivity;
import com.uidt.qmrz_zy.mvp.contract.CommonContract;
import com.uidt.qmrz_zy.mvp.model.CommonModel;
import com.uidt.qmrz_zy.mvp.presenter.CommonPresenter;
import com.uidt.qmrz_zy.utils.DialogUtils;
import com.uidt.qmrz_zy.utils.FileUtil;

import java.io.File;

public class MainActivity extends BaseActivity<CommonPresenter, CommonModel> implements CommonContract.View, View.OnClickListener {

    private RelativeLayout rlGskkgl;

    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;

    private boolean hasGotToken = false;

    private AlertDialog.Builder alertDialog;

    private boolean checkGalleryPermission() {
        int ret = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        DialogUtils.getDialogUtils(MainActivity.this).show();

        rlGskkgl = findViewById(R.id.rl_main_gskkdgl);
        rlGskkgl.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(this);

        checkGalleryPermission();

        initAccessTokenWithAkSk();
    }

    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DialogUtils.getDialogUtils(MainActivity.this).show();

        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        //后置
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        //前置
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        } else {
            DialogUtils.getDialogUtils(MainActivity.this).dismiss();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                DialogUtils.getDialogUtils(MainActivity.this).dismiss();
                if (result != null) {
                    String resultInfo = result.toString();
                    if (resultInfo.contains("address") && resultInfo.contains("idNumber")){
                        int start = resultInfo.indexOf("{");
                        int end = resultInfo.indexOf("}");
                        String info = resultInfo.substring(start,end);
                        String[] infos = info.split(",");
                        if (infos.length > 4) {
                            String address = infos[2].substring(9, infos[2].length());
                            String idNumber = infos[3].substring(10, infos[3].length());
                            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(idNumber) || address.equals("null")) {
                                alertText("", "身份证件识别失败！");
                            } else {
                                SureInfoActivity.startAction(MainActivity.this,idNumber,address);
                            }
                        } else {
                            alertText("", "身份证件识别失败！");
                        }
                    } else {
                        alertText("", "身份证件识别失败！");
                    }
                }
            }

            @Override
            public void onError(OCRError error) {
                DialogUtils.getDialogUtils(MainActivity.this).dismiss();
                alertText("", error.getMessage());
            }
        });
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;

                DialogUtils.getDialogUtils(MainActivity.this).dismiss();

                //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
                CameraNativeHelper.init(MainActivity.this, OCR.getInstance(MainActivity.this).getLicense(),
                        new CameraNativeHelper.CameraNativeInitCallback() {
                            @Override
                            public void onError(int errorCode, Throwable e) {
                                String msg;
                                switch (errorCode) {
                                    case CameraView.NATIVE_SOLOAD_FAIL:
                                        msg = "加载so失败，请确保apk中存在ui部分的so";
                                        break;
                                    case CameraView.NATIVE_AUTH_FAIL:
                                        msg = "授权本地质量控制token获取失败";
                                        break;
                                    case CameraView.NATIVE_INIT_FAIL:
                                        msg = "本地质量控制";
                                        break;
                                    default:
                                        msg = String.valueOf(errorCode);
                                }
                                Looper.prepare();
                                Toast.makeText(mContext, "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        });
            }

            @Override
            public void onError(OCRError error) {
                DialogUtils.getDialogUtils(MainActivity.this).dismiss();
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "MACLGGDMiUrIr0Vcnp373nvG", "q5mvOVYaRedPsdLfTuzLvruyAjNGOqsY");
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void alertText(final String title, final String message) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    alertDialog.setTitle(title)
                            .setMessage(message)
                            .setPositiveButton("确定", null)
                            .show();
                }

            });
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
            case R.id.rl_main_gskkdgl:
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
//                        true);
//                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
//                // 请手动使用CameraNativeHelper初始化和释放模型
//                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
//                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
//                        true);
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            default:
        }
    }
}
