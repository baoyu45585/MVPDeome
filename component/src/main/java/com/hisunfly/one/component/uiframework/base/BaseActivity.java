package com.hisunfly.one.component.uiframework.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.hisunfly.one.component.R;
import com.hisunfly.one.component.exception.AppException;
import com.hisunfly.one.component.refresh.IRefreshLayout;
import com.hisunfly.one.component.refresh.RefreshType;
import com.hisunfly.one.component.uiframework.ViewState;
import com.hisunfly.one.component.uiframework.inface.LceelDelegate;
import com.hisunfly.one.component.uiframework.inface.LceelView;
import com.hisunfly.one.component.uiframework.inface.OnViewStateListener;
import com.hisunfly.one.component.uiframework.inface.TitleView;
import com.hisunfly.one.component.uiframework.inface.TracePointListener;
import com.hisunfly.one.component.utils.NetworkUtils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;




/**
 * description:activity基类
 *
 * @author: jinyuef
 * @Create Time :2018/1/22
 */
public abstract class BaseActivity extends CompatActivity implements LceelView,TracePointListener {
    private LceelDelegateImpl mLceeDelegate;
    private TitleView mTitleView;
    private boolean mToolbarSeted = false;

    /**
     * activity是否在前台
     */
    private boolean isActivityVisibleToUser;

    /**
     * desc:处理业务相关的初始化操作,不可被覆写
     * <p/>
     * author: jinyuef
     * date: 2018/1/22
     */
    @Override
    protected final void onBeforeinit0(Bundle savedInstanceState) {

    }



    @Override
    protected void onResume() {
        isActivityVisibleToUser = true;
        super.onResume();

    }

    @Override
    protected void onPause() {
        isActivityVisibleToUser = false;
        super.onPause();
    }

    @Override
    protected final void setContainerView() {
        if (getLayout() == -1) {
            return;
        }
//        setContentView(getLayout());
        mLceeDelegate = new LceelDelegateImpl(this, getLayout(), getRefreshType(), new
                MyViewStateListener());
        mLceeDelegate.setup();
    }



    public final TitleView getTitleView() {
        ensureTitleView(isContentUnderTitleBar());
        return mTitleView;
    }

    private void ensureTitleView(boolean isContentUnderTitleBar) {
        if (mTitleView == null) {
            mTitleView = mLceeDelegate.getTitleView(isFinalImmersionBarEnable(),
                    isContentUnderTitleBar);
        }
        if (!mToolbarSeted && mTitleView != null) {
            if (mTitleView.getToolbar() != null) {
                setSupportActionBar(mTitleView.getToolbar());
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            mTitleView.setNavIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processClickNavIcon();
                }
            });
            mToolbarSeted = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Desc:设置内容在标题栏下面
     * <p>
     * Author: jinyuef
     * Date: 2018-08-24
     *
     * @param isContentUnderTitleBar
     */
    protected void setContentUnderTitleBar(boolean isContentUnderTitleBar) {
        if(mTitleView == null){
            ensureTitleView(isContentUnderTitleBar);
        }else{
            mLceeDelegate.layoutTitleView(isFinalImmersionBarEnable(),isContentUnderTitleBar,false);
        }
    }

    /**
     * desc:处理点击后退图标
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    protected void processClickNavIcon() {
        if (beforeExit()) {
            return;
        }
        onBackPressed();
    }

    /**
     * 标题栏返回前的动作，是否需要拦截
     *
     * @return
     */
    public boolean beforeExit() {
        return false;
    }

    /**
     * desc:内容区域是否在标题栏之下
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    public boolean isContentUnderTitleBar() {
        return false;
    }

    /**
     * Desc:获取刷新类别({@link RefreshType})
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @return int
     */
    protected @RefreshType.Val
    int getRefreshType() {
        return RefreshType.NONE;
    }

    /**
     * 页面状态控制
     **/
    @Override
    public void setState(ViewState viewState, Object... args) {
        if (viewState == ViewState.STATE_ERROR) {
            processStateError(true, args);
        } else {
            mLceeDelegate.showState(viewState, true, true, args);
        }
    }

    @Override
    public void showState(ViewState viewState, Object... args) {
        if (viewState == ViewState.STATE_ERROR) {
            processStateError(false, args);
        } else {
            mLceeDelegate.showState(viewState, true, false, args);
        }
    }

    protected void hideState(ViewState viewState) {
        mLceeDelegate.showState(viewState, false, false);
    }

    /**
     * desc:处理页面错误状态
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    private void processStateError(boolean hideOther, Object... args) {
        try {
            List argList = Arrays.asList(args);
            if (argList != null && argList.size() > 0) {
                Object obj = argList.get(0);
                if (obj instanceof AppException) {
                    setStateError(hideOther, (AppException) obj);
                } else {
                    mLceeDelegate.showState(ViewState.STATE_ERROR, true, hideOther, args);
                }
            } else {
                mLceeDelegate.showState(ViewState.STATE_ERROR, true, hideOther, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLceeDelegate.showState(ViewState.STATE_ERROR, true, hideOther, args);
        }
    }

    /**
     * desc:处理页面错误状态
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    private void setStateError(boolean hideOther, AppException e) {
        int strResId = R.string.common_load_fail_tips;
        int drawableResId = R.drawable.common_ic_load_failure;
       /* if (e instanceof NetworkTimeOutException) {
            strResId = R.string.exception_fail_net_msg;
        }*/
        if (!NetworkUtils.isNetworkAvailable()) {
            strResId = R.string.common_load_fail_bad_net;
            drawableResId = R.drawable.common_ic_no_network;
        }
        mLceeDelegate.showState(ViewState.STATE_ERROR, hideOther, hideOther, strResId,
                drawableResId);
    }

    @Override
    protected final View getContentView() {
        return mLceeDelegate.getContentView();
    }

    @Override
    protected View getRootView() {
        return mLceeDelegate.getView();
    }

    /**
     * Desc:获取页面状态操作代理类（子类不可重写）
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @return lceel delegate
     */
    @Override
    public final LceelDelegate getLceeDelegate() {
        return mLceeDelegate;
    }

    /**
     * Desc:获取刷新控件接口(可由子类重写)
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout getRefreshLayout() {
        return mLceeDelegate != null ? mLceeDelegate.getRefreshLayout() : null;
    }



    /**
     * desc:处理错误状态页面重新加载（子类覆写）
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    protected void onPageReload(View v) {

    }

    /**
     * desc:处理无数据状态页面按钮（子类覆写）
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    protected void processNodataBtnClick(View v) {

    }

    /**
     * desc:处理登录完成后的逻辑（子类覆写）
     * <p/>
     * author: jinyuef
     * date: 2018/4/13
     */
    protected void processAfterLogin() {

    }

    /**
     * Desc:刷新时回调
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @param refreshLayout
     */
    protected void processRefresh(IRefreshLayout refreshLayout) {

    }

    /**
     * Desc:加载更多时回调
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @param refreshLayout
     */
    protected void processLoadMore(IRefreshLayout refreshLayout) {

    }

    private class MyViewStateListener implements OnViewStateListener {

        @Override
        public void onReload(View v) {
            onPageReload(v);
        }

        @Override
        public void onNodataBtnClick(View v) {
            processNodataBtnClick(v);
        }

        @Override
        public void onAfterLogin() {
            processAfterLogin();
        }

        @Override
        public void onRefresh(IRefreshLayout refreshLayout) {
            processRefresh(refreshLayout);
        }

        @Override
        public void onLoadMore(IRefreshLayout refreshLayout) {
            processLoadMore(refreshLayout);
        }
    }

    /**
     * 取数特殊页面id，有则重写并传值.
     * 例如趣拍详情页，传趣拍id
     * @return
     */
    @Override
    public String getTraceTargetID(){
        return "";
    }

    @Override
    public void onTracePoint(HashMap<Integer, String> parameter){

    }

    public boolean isActivityVisibleToUser() {
        return isActivityVisibleToUser;
    }
}
