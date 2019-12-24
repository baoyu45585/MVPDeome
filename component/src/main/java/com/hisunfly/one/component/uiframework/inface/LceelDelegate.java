package com.hisunfly.one.component.uiframework.inface;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;

import com.hisunfly.one.component.refresh.IRefreshLayout;
import com.hisunfly.one.component.uiframework.ViewState;


/**
 * description:
 *
 * @author: jinyuef
 * @Create Time:2018/2/11
 */

public interface LceelDelegate {
    void setTitleLayoutLayout(@LayoutRes int layoutResId);

    void setEmptyLayout(@LayoutRes int layoutResId);

    void setLoadingLayout(@LayoutRes int layoutResId);

    void setErrorLayout(@LayoutRes int layoutResId);

    void setLoginLayoutId(@LayoutRes int loginLayoutId);

    void setEmptyText(@StringRes int text);

    void setEmptyText(String text);

    void setEmptyTextColor(@ColorRes int color);

    void setEmptyIcon(@DrawableRes int icon);

    void setEmptyBtnTxt(String text);

    View getEmptyView();

    View getErrorView();

    View getContentView();

    View getLoadingView();

    ViewState getViewState();

    IRefreshLayout getRefreshLayout();
}
