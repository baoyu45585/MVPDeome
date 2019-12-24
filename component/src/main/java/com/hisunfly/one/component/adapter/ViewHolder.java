//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hisunfly.one.component.R.id;

public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int postion) {
        this.mContext = context;
        this.mViews = new SparseArray();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(id.holder, this);
    }

    public static ViewHolder get(Context context, View converView, ViewGroup parent, int layoutId, int position) {
        return converView == null ? new ViewHolder(context, parent, layoutId, position) : (ViewHolder)converView.getTag(id.holder);
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = (View)this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public ViewHolder setText(@IdRes int viewId, String text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolder setText(@IdRes int viewId, Spanned text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(resId);
        return this;
    }

    public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int drawableId) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public ViewHolder setImageDrawable(@IdRes int viewId, Drawable mDrawable) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageDrawable(mDrawable);
        return this;
    }

    public ViewHolder setViewBackGround(@IdRes int viewId, @DrawableRes int drawableId) {
        this.getView(viewId).setBackgroundResource(drawableId);
        return this;
    }
}
