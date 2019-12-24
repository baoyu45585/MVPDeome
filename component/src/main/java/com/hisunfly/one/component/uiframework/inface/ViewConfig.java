package com.hisunfly.one.component.uiframework.inface;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

import com.hisunfly.one.component.R;



/**
 * description:视图控制器
 *
 * @author: jinyuef
 * @Create Time:2017/10/13
 */
public class ViewConfig {
    public @LayoutRes
    int titleLayoutId = R.layout.base_toolbar;
    public @LayoutRes
    int loadingLayoutId = R.layout.stub_progress_wait;
    public @LayoutRes
    int errorLayoutId = R.layout.stub_reload_layout;
    public @LayoutRes
    int emptyLayoutId = R.layout.stub_no_data;
    public @LayoutRes
    int loginLayoutId = R.layout.stub_login_layout;

    public @DrawableRes
    int emptyDrableId = -1;
    public String emptyTxt;
    public @ColorRes int emptyTxtColor = 0;
    public String emptyBtnTxt;

    public void setTitleLayoutId(@LayoutRes int titleLayoutId) {
        this.titleLayoutId = titleLayoutId;
    }

    public void setLoadingLayoutId(@LayoutRes int loadingLayoutId) {
        this.loadingLayoutId = loadingLayoutId;
    }

    public void setErrorLayoutId(@LayoutRes int errorLayoutId) {
        this.errorLayoutId = errorLayoutId;
    }

    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId) {
        this.emptyLayoutId = emptyLayoutId;
    }

    public void setLoginLayoutId(@LayoutRes int loginLayoutId) {
        this.loginLayoutId = loginLayoutId;
    }

    public int getEmptyDrableId() {
        return emptyDrableId;
    }

    public void setEmptyDrableId(@DrawableRes int emptyDrableId) {
        this.emptyDrableId = emptyDrableId;
    }

    public String getEmptyTxt() {
        return emptyTxt;
    }

    public void setEmptyTxt(String emptyTxt) {
        this.emptyTxt = emptyTxt;
    }

    public String getEmptyBtnTxt() {
        return emptyBtnTxt;
    }

    public void setEmptyBtnTxt(String emptyBtnTxt) {
        this.emptyBtnTxt = emptyBtnTxt;
    }

    public int getTitleLayoutId() {
        return titleLayoutId;
    }

    public int getLoadingLayoutId() {
        return loadingLayoutId;
    }

    public int getErrorLayoutId() {
        return errorLayoutId;
    }

    public int getEmptyLayoutId() {
        return emptyLayoutId;
    }

    public int getLoginLayoutId() {
        return loginLayoutId;
    }

    public @ColorRes int getEmptyTxtColor() {
        return emptyTxtColor;
    }

    public void setEmptyTxtColor( @ColorRes int emptyTxtColor) {
        this.emptyTxtColor = emptyTxtColor;
    }
}
