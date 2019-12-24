package com.hisunfly.one.component.refresh;

import android.support.annotation.NonNull;


/**
 * description:刷新控件回调
 *
 * @author: jinyuef
 * @Create Time:2018/4/26
 */
public interface OnRefreshAndLoadMoreListener {
    void onRefresh(@NonNull IRefreshLayout refreshLayout);
    void onLoadMore(@NonNull IRefreshLayout refreshLayout);
}
