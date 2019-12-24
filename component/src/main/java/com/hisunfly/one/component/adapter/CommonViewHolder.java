//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class CommonViewHolder extends ViewHolder {
    private final SparseArray<View> mViews = new SparseArray();
    private final LinkedHashSet<Integer> childClickViewIds = new LinkedHashSet();

    public static CommonViewHolder create(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public void clearViews() {
        this.mViews.clear();
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = (View)this.mViews.get(viewId);
        if (view == null) {
            view = this.itemView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return this.itemView;
    }

    public CommonViewHolder setText(@IdRes int viewId, String text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setText(@IdRes int viewId, Spanned text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(resId);
        return this;
    }

    public CommonViewHolder setImageResource(@IdRes int viewId, @DrawableRes int drawableId) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public CommonViewHolder setImageDrawable(@IdRes int viewId, Drawable mDrawable) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageDrawable(mDrawable);
        return this;
    }

    public CommonViewHolder setViewBackGround(@IdRes int viewId, @DrawableRes int drawableId) {
        this.getView(viewId).setBackgroundResource(drawableId);
        return this;
    }

    public CommonViewHolder setVisibility(@IdRes int viewId, int visibility) {
        this.getView(viewId).setVisibility(visibility);
        return this;
    }

    public CommonViewHolder setOnClickListener(@IdRes int viewId, OnClickListener l) {
        View view = this.getView(viewId);
        if (view != null) {
            view.setOnClickListener(l);
        }

        return this;
    }

    public CommonViewHolder setOnLongClickListener(@IdRes int viewId, OnLongClickListener l) {
        View view = this.getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(l);
        }

        return this;
    }

    public CommonViewHolder setOnClickListener(OnClickListener l) {
        if (this.itemView != null) {
            this.itemView.setOnClickListener(l);
        }

        return this;
    }

    public CommonViewHolder setOnLongClickListener(OnLongClickListener l) {
        if (this.itemView != null) {
            this.itemView.setOnLongClickListener(l);
        }

        return this;
    }

    public CommonViewHolder setTag(@IdRes int viewId, Object tag) {
        this.getView(viewId).setTag(tag);
        return this;
    }

    public HashSet<Integer> getChildClickViewIds() {
        return this.childClickViewIds;
    }

    public CommonViewHolder addOnChildClickListener(int viewId) {
        this.childClickViewIds.add(viewId);
        return this;
    }

    public CommonViewHolder removeOnClick(int viewId) {
        this.childClickViewIds.remove(viewId);
        return this;
    }
}
