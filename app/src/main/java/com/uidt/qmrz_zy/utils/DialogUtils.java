package com.uidt.qmrz_zy.utils;

import android.app.Activity;
import android.graphics.Color;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * @author yijixin on 2020-02-12
 */
public class DialogUtils {
    private static DialogUtils dialogUtils = null;
    private ZLoadingDialog dialogLoading = null;

    public static DialogUtils getDialogUtils(Activity activity) {
        if (dialogUtils == null) {
            synchronized (DialogUtils.class) {
                if (dialogUtils == null) {
                    dialogUtils = new DialogUtils(activity);
                }
            }
        }
        return dialogUtils;
    }

    public DialogUtils(Activity activity) {
        if (dialogLoading == null) {
            dialogLoading = showLoading(activity);
        }
    }

    private ZLoadingDialog showLoading(Activity activity) {
        ZLoadingDialog dialog = new ZLoadingDialog(activity);
        //设置类型
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)
                //颜色
                .setLoadingColor(Color.WHITE)
                .setHintText("Loading...")
                // 设置字体大小 dp
                .setHintTextSize(13)
                // 设置字体颜色
                .setHintTextColor(Color.WHITE)
                // 设置动画时间百分比 - 0.5倍
                .setDurationTime(0.5)
                // 设置背景色，默认白色
                .setDialogBackgroundColor(Color.parseColor("#CC111111"))
                .setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void show() {
        dialogLoading.show();
    }

    public void dismiss() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
        if (dialogUtils != null) {
            dialogUtils = null;
        }
    }
}
