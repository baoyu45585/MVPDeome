//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends ViewHolder {
    private final SparseArray<View> mViews = new SparseArray();
    private View mConvertView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mConvertView = itemView;
        this.mConvertView.setTag(this);
    }

    public <T extends View> T getView(int viewId) {
        View view = (View)this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public RecyclerViewHolder setText(int viewId, CharSequence text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public RecyclerViewHolder setText(int viewId, int textResId) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(textResId);
        return this;
    }

    public RecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }
}
