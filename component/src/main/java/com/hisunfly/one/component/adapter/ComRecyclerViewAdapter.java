//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.util.List;

public abstract class ComRecyclerViewAdapter<T> extends Adapter<RecyclerViewHolder> {
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> list;
    private ComRecyclerViewAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(ComRecyclerViewAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public ComRecyclerViewAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.list = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.mInflater.inflate(this.mItemLayoutId, parent, false);
        RecyclerViewHolder vh = new RecyclerViewHolder(itemView);
        return vh;
    }

    public T getItem(int position) {
        return this.list == null ? null : this.list.get(position);
    }

    public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {
        this.convert(viewHolder, this.getItem(position), position);
        if (this.mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    if (pos > -1) {
                        ComRecyclerViewAdapter.this.mOnItemClickLitener.onItemClick(viewHolder.itemView, ComRecyclerViewAdapter.this.list.get(pos), pos);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    if (pos > -1) {
                        ComRecyclerViewAdapter.this.mOnItemClickLitener.onItemLongClick(viewHolder.itemView, ComRecyclerViewAdapter.this.list.get(pos), pos);
                    }

                    return false;
                }
            });
        }

    }

    public abstract void convert(RecyclerViewHolder var1, T var2, int var3);

    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public void setData(List<T> data) {
        this.list = data;
    }

    public interface OnItemClickLitener<T> {
        void onItemClick(View var1, T var2, int var3);

        void onItemLongClick(View var1, T var2, int var3);
    }
}
