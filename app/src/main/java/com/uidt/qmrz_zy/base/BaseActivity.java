package com.uidt.qmrz_zy.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uidt.qmrz_zy.utils.TUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import qiu.niorgai.StatusBarCompat;

public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity {

    public P mPresenter;
    public M mModel;
    public Context mContext;
    protected RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;

        StatusBarCompat.translucentStatusBar(this,true);

        setStatusBarLightMode(this,true);

        rxPermissions = new RxPermissions(this);

        //加入Activity管理器
        AppManager.getAppInstance().addActivity(this);


        if (TUtils.getT(this, 0) != null) {
            mPresenter = TUtils.getT(this, 0);
        }
        if (TUtils.getT(this, 1) != null) {
            mModel = TUtils.getT(this, 1);
        }
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }

        initPresenter();
        initViews(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppInstance().finishActivity(this);
    }

    /**
     * 运行在onResume()之后
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 是否需要EventBus 需要重写该方法并返回true
     */
    protected Boolean reqEvent(){
        return false;
    }

    /**
     * 设置布局Layout
     *
     * @return 返回布局文件
     */
    public abstract int getLayoutId();

    /**
     * 初始化控件
     *
     * @param savedInstanceState .
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public abstract void initPresenter();


    /**
     * 设置状态栏文字颜色  true 为黑色  false 为白色（XML跟布局配置  android:fitsSystemWindows="true"，否则布局会上移）
     * 魅族和miui单独判断
     */
    public static void setStatusBarLightMode(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (dark) {
                if (mIuiSetStatusBarLightMode(activity, dark)) {
                } else if (flymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //其他
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (mIuiSetStatusBarLightMode(activity, dark)) {
                } else if (flymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
    }

    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
    public static boolean mIuiSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }
}
