package com.hisunfly.one.component.refresh;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;


/**
 * Desc::刷新控件代理类(主要是考虑后期升级方法和实现可能会变更或刷新控件替换)
 * <p>
 * Author: jinyuef
 * Date: 2018-04-25
 * Copyright: Copyright (c) 2010-2018
 * Company: @咪咕动漫
 * Updater:
 * Update Time:
 * Update Comments:
 */
public class RefreshLayoutProxy implements IRefreshLayout {
    private SmartRefreshLayout mRefreshLayout;
    private OnRefreshAndLoadMoreListener mListener;

    public RefreshLayoutProxy(SmartRefreshLayout refreshLayout) {
        this.mRefreshLayout = refreshLayout;
        init();
    }

    public RefreshLayoutProxy(SmartRefreshLayout mRefreshLayout, OnRefreshAndLoadMoreListener listener) {
        this.mRefreshLayout = mRefreshLayout;
        this.mListener = listener;
        init();
    }

    /**
     * Desc:初始化
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     */
    private void init() {
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableNestedScroll(false);
        mRefreshLayout.setEnableFooterTranslationContent(true);
        mRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener(){

           @Override
           public void onRefresh(RefreshLayout refreshLayout) {
                   if(mListener != null){
                       mListener.onRefresh(RefreshLayoutProxy.this);
                   }
           }

           @Override
           public void onLoadMore(RefreshLayout refreshLayout) {
                   if(mListener !=  null){
                       mListener.onLoadMore(RefreshLayoutProxy.this);
                   }
           }
       });
    }

    /**
     * Desc:设置刷新控件回调
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @param listener
     */
    @Override
    public IRefreshLayout setRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * Desc:设置是否可以刷新
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @param enable
     * @return refresh layout
     */
    @Override
    public IRefreshLayout setEnableRefresh(boolean enable) {
        mRefreshLayout.setEnableRefresh(enable);
        return this;
    }

    /**
     * Desc:设置是否可以加载更多
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @param enable
     * @return refresh layout
     */
    @Override
    public IRefreshLayout setEnableLoadMore(boolean enable) {
        mRefreshLayout.setEnableLoadMore(enable);
        return this;
    }

    /**
     * Desc:结束刷新
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     */
    @Override
    public IRefreshLayout finishRefresh() {
        mRefreshLayout.finishRefresh();
        return this;
    }

    /**
     * Desc:带有延迟的结束刷新
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param delayed
     * @return refresh layout
     */
    @Override
    public IRefreshLayout finishRefresh(int delayed) {
        mRefreshLayout.finishRefresh(delayed);
        return this;
    }

    /**
     * Desc:自动刷新
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout autoRefresh() {
        mRefreshLayout.autoRefresh();
        return this;
    }
    /**
     * Desc:带有延迟的自动刷新
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout autoRefresh(int delayed) {
        mRefreshLayout.autoRefresh(delayed);
        return this;
    }

    /**
     * Desc:完成加载更多
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout finishLoadMore() {
        mRefreshLayout.finishLoadMore();
        return this;
    }
    /**
     * Desc:完成加载更多
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout finishLoadMore(int delayed) {
        mRefreshLayout.finishLoadMore(delayed);
        return this;
    }

    /**
     * Desc:完成加载更多并提示，并不再触发加载更事件
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout finishLoadMoreWithNoMoreData() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.setNoMoreData(true);
        return this;
    }
    /**
     * Desc:完成加载更多但不提示，并不再触发加载更事件
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout finishLoadMoreWithNoMoreDataAndNoTip() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.setEnableLoadMore(false);
        return this;
    }

    /**
     * Desc:设置数据全部加载完成，将不能再次触发加载功能
     * <p>
     * Author: jinyuef
     * Date: 2018-04-26
     *
     * @param finished 数据是否加载完成
     * @return refresh layout
     */
    @Override
    public IRefreshLayout setLoadmoreFinished(boolean finished) {
        if(!finished){
            mRefreshLayout.setEnableLoadMore(true);
        }
        mRefreshLayout.setNoMoreData(finished);
        return this;
    }

    @Override
    public IRefreshLayout setEnableNestedScroll(boolean enabled) {
        mRefreshLayout.setEnableNestedScroll(enabled);
        return this;
    }
}
