//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hisunfly.one.component.uiframework.ViewState;
import com.hisunfly.one.component.utils.EventBusUtils;


public abstract class CompatMvpPresenter<T extends RxMvpView> implements MvpPresenter {
    protected String TAG = this.getClass().getSimpleName();
    protected T mRxMvpView;

    public CompatMvpPresenter(T view) {
        this.mRxMvpView = view;
        if (this.isApplyEventBus()) {
            EventBusUtils.register(this);
        }

    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onPause() {
    }

    public void stop() {
    }

    public void resume() {
    }

    public void onDestroy() {
        if (this.isApplyEventBus()) {
            EventBusUtils.unregister(this);
        }

    }

    public void getData(Object... args) {
    }

    public void onShow() {
    }

    public void onHide() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    protected Context getContext() {
        return this.mRxMvpView.getContext();
    }

    protected Activity getActivity() {
        return (Activity)this.mRxMvpView.getContext();
    }

    protected void setState(ViewState viewState, Object... args) {
        this.mRxMvpView.setState(viewState, args);
    }

    protected boolean isApplyEventBus() {
        return false;
    }
}
