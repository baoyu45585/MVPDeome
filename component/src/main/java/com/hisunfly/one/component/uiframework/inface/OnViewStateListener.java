package com.hisunfly.one.component.uiframework.inface;

import android.view.View;

import com.hisunfly.one.component.refresh.IRefreshLayout;


/**
 * description:
 *
 * @author: jinyuef
 * @Create Time:2018/4/5
 */
public interface OnViewStateListener {
    void onReload(View v);
    void onNodataBtnClick(View v);
    void onAfterLogin();
    void onRefresh(IRefreshLayout refreshLayout);
    void onLoadMore(IRefreshLayout refreshLayout);
}
