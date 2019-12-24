package com.hisunfly.one.component.refresh;

/**
 * description:定义刷新控件接口（主要是考虑后期升级方法可能会变或刷新控件替换）
 *
 * @author: jinyuef
 * @Create Time:2018/4/25
 */
public interface IRefreshLayout {
    IRefreshLayout setRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener listener);
    IRefreshLayout setEnableRefresh(boolean enable);
    IRefreshLayout setEnableLoadMore(boolean enable);
    IRefreshLayout finishRefresh();
    IRefreshLayout finishRefresh(int delayed);
    IRefreshLayout autoRefresh();
    IRefreshLayout autoRefresh(int delayed);

    IRefreshLayout finishLoadMore();
    IRefreshLayout finishLoadMore(int delayed);
    IRefreshLayout finishLoadMoreWithNoMoreData();
    IRefreshLayout finishLoadMoreWithNoMoreDataAndNoTip();
    IRefreshLayout setLoadmoreFinished(boolean finished);

    IRefreshLayout setEnableNestedScroll(boolean enabled);
}
