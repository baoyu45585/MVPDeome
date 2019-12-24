package com.hisunfly.one.component.uiframework.inface;


import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public interface TitleView {
    void setBackgroundColor(@ColorInt int color);
    void setVisibility(int visibility);
    void setImmersionBarEnable(boolean isImmersionBarEnable);

    TitleView setNavIconOnClickListener(View.OnClickListener onClickListener);
    TitleView setNavIcon(@Nullable Drawable icon);
    TitleView setNavIcon(@DrawableRes int resId);
    TitleView setNavIconVisibility(boolean isVisible);
    TitleView setLeftText(String leftTxt, View.OnClickListener onClickListener);
    TitleView setLeftText(String leftTxt);
    TitleView setLeftTextColor(@ColorInt int color);
    TitleView setLeftTextSize(int sizeOfSp);
    TextView getLeftText();

    TitleView  setTitleText(CharSequence title);
    TitleView  setTitleText(@StringRes int title);
    TitleView  setTitleTextColor(@ColorInt int color);
    TitleView  setTitleTextSize(int sizeOfSp);
    TitleView setTitleGravityLeft();
    TextView   getTitleText();
    Toolbar getToolbar();

    <T extends View> T  setCenterView(@LayoutRes int layoutId);
    <T extends View> T  setLayoutView(@LayoutRes int layoutId);

    TitleView  addMenuItem(View view, View.OnClickListener onClickListener);
    TitleView  addMenuItem(ViewGroup viewGroup);
    TextView addMenuItem(String text, View.OnClickListener onClickListener);
    void  clearMenuItems();
}
