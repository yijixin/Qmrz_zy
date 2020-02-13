package com.uidt.qmrz_zy.mvp.presenter;

import com.uidt.qmrz_zy.base.RxSubscriber;
import com.uidt.qmrz_zy.bean.QmkyLevelBean;
import com.uidt.qmrz_zy.mvp.contract.SureInfoContract;

/**
 * @author yijixin on 2020-02-13
 */
public class SureInfoPresenter extends SureInfoContract.Presenter {
    @Override
    public void passlevelInfos(String personalid, String addr, String name, int gender, String birthdate, String nationality) {
        mRxManager.add(mModel.getPassResult(personalid, addr, name, gender, birthdate, nationality).subscribe(new RxSubscriber<QmkyLevelBean>(mContext) {
            @Override
            protected void _Next(QmkyLevelBean qmkyLevelBean) {
                mView.loadPassResult(qmkyLevelBean);
            }

            @Override
            protected void _Error(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
