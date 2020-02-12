package com.uidt.qmrz_zy.base;


import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by yijixin at 2017/11/8
 */
public class RxManager {
    /**
     * 管理Observables 和 Subscribers订阅
     */
    private CompositeDisposable mDisposable = new CompositeDisposable();

    /**
     * 单纯的Observables 和 Subscribers管理
     * @param disposable
     */
    public void add(Disposable disposable){
        /**
         * 订阅管理
         */
        mDisposable.add(disposable);
    }

    /**
     * 单个presenter生命周期结束，取消订阅
     */
    public void clear(){
        /**
         * 取消订阅
         */
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
