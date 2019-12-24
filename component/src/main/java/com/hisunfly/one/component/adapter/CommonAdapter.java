//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> list;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.list = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public boolean hasStableIds() {
        return true;
    }

    public int getCount() {
        return this.list == null ? 0 : this.list.size();
    }

    public T getItem(int position) {
        return this.list == null ? null : this.list.get(position);
    }

    public long getItemId(int position) {
        return (long)this.list.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = this.getViewHolder(position, convertView, parent);
        this.convert(viewHolder, this.getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder var1, T var2, int var3);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(this.mContext, convertView, parent, this.mItemLayoutId, position);
    }

    public void setData(List<T> data) {
        this.list = data;
    }
}
