package com.hisunfly.one.component.uiframework.mvp;



import com.hisunfly.one.component.refresh.IRefreshLayout;
import com.hisunfly.one.component.uiframework.inface.LceelDelegate;
import com.hisunfly.one.component.uiframework.inface.LceelView;

/**
 * Desc:基础presenter
 * <p>
 * Author: jinyuef
 * Date: 2018-06-29
 * Copyright: Copyright (c) 2010-2018
 * Company: @咪咕动漫
 * Updater:
 * Update Time:
 * Update Comments:
 */
public class BaseMvpPresenter<T extends RxMvpView, U extends LceelView> extends
        CompatMvpPresenter<T> {
    protected U mLceelView;

    public BaseMvpPresenter(T view, U lceelView) {
        super(view);
        this.mLceelView = lceelView;
    }

    /**
     * Desc:获取页面状态代理类
     * <p>
     * Author: jinyuef
     * Date: 2018-06-29
     *
     * @return lceel delegate
     */
    protected LceelDelegate getLceeDelegate() {
        return mLceelView.getLceeDelegate();
    }

    /**
     * Desc:获取刷新控件代理类
     * <p>
     * Author: jinyuef
     * Date: 2018-06-29
     *
     * @return refresh layout
     */
    public IRefreshLayout getRefreshLayout() {
        return mLceelView.getRefreshLayout();
    }

}
