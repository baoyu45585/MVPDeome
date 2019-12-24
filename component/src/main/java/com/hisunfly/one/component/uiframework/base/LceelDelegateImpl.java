package com.hisunfly.one.component.uiframework.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.hisunfly.one.component.R;
import com.hisunfly.one.component.refresh.IRefreshLayout;
import com.hisunfly.one.component.refresh.OnRefreshAndLoadMoreListener;
import com.hisunfly.one.component.refresh.RefreshLayoutProxy;
import com.hisunfly.one.component.refresh.RefreshType;
import com.hisunfly.one.component.uiframework.ViewState;
import com.hisunfly.one.component.uiframework.inface.LceelDelegate;
import com.hisunfly.one.component.uiframework.inface.OnViewStateListener;
import com.hisunfly.one.component.uiframework.inface.TitleView;
import com.hisunfly.one.component.uiframework.inface.ViewConfig;

import com.hisunfly.one.component.utils.BarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


/**
 * Desc:Loading-Content-Empty-Error-login状态转换
 * <p>
 * Author: jinyuef
 * Date: 2018-04-25
 * Copyright: Copyright (c) 2010-2018
 * Company: @咪咕动漫
 * Updater:
 * Update Time:
 * Update Comments:
 */
public class LceelDelegateImpl implements LceelDelegate {
    /**
     * 当前页面状态
     */
    private ViewState mCurrentState = ViewState.STATE_COMPLETED;
    private Context mContext;
    private CompatActivity mActivity;
    private @RefreshType.Val
    int mRefreshType = RefreshType.NONE;
    private IRefreshLayout mRefreshLayout;
    private OnViewStateListener mOnViewStateListener;
    private ViewConfig mViewConfig;
    private Fragment mFragment;
    private int mContentLayoutResID;
    private View mView;//页面base View
    private LayoutInflater mLayoutInflater;
    private View mContentView;////内容布局
    private View mLoadingView;//加载中布局
    private View mEmptyView;//无数据布局
    private View mErrorView;//加载失败布局
    private View mLoginView;//提示登录布局
    private View mTitleView;//标题布局
    private View mContentWrapView;

    public ImageView mErrorImgv;
    public TextView mErrorTv;
    public TextView mRetryTv;

    private ImageView mEmptyImgv;// 无数据显示的图片
    private TextView mEmptyTv;// 无数据提示文字
    private TextView mEmptyBtn;//无数据功能按钮

    View mLoginTv;//马上登陆按钮


    LceelDelegateImpl(CompatActivity activity, int contentLayoutRes, @RefreshType.Val int refreshType, final OnViewStateListener onViewStateListener) {
        mActivity = activity;
        mContext = activity;
        mRefreshType = refreshType;
        mContentLayoutResID = contentLayoutRes;
        this.mOnViewStateListener = onViewStateListener;
        init();
    }

    LceelDelegateImpl(Fragment fragment, int contentLayoutRes, @RefreshType.Val int refreshType, final OnViewStateListener onViewStateListener) {
        mFragment = fragment;
        mContext = fragment.getActivity();
        mRefreshType = refreshType;
        mContentLayoutResID = contentLayoutRes;
        this.mOnViewStateListener = onViewStateListener;
        init();
    }

    /**
     * Desc:初始化
     * Author: jinyuef
     * Date: 2018-04-25
     */
    private void init() {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        mViewConfig = new ViewConfig();
    }

    View setup() {
        if (mView == null) {
            View mainView = null;
            if (mActivity != null) {
                mActivity.setContentView(getContentLayoutResId());
                mainView = mActivity.findViewById(R.id.lceel_main);
            } else if (mFragment != null) {
                mainView = mLayoutInflater.inflate(getContentLayoutResId(), null);
            }
            mView = mainView;
            setupContentView();
            if (isNeedRefreshLayout()) {
                SmartRefreshLayout smartRefreshLayout = (SmartRefreshLayout) mainView.findViewById(R.id.content_wrap);
                setupRefreshLayout(smartRefreshLayout);
            }
            mContentWrapView = mainView.findViewById(R.id.content_wrap);
        }
        return mView;
    }


    private int getContentLayoutResId() {
        return isNeedRefreshLayout() ? R.layout.base_lceel_with_refresh : R.layout.base_lceel;
    }

    private boolean isNeedRefreshLayout() {
        return mRefreshType != RefreshType.NONE;
    }

    private void setupContentView() {
        ViewStub contentStub = (ViewStub) mView.findViewById(R.id.stub_content);
        if (contentStub != null) {
            contentStub.setLayoutResource(mContentLayoutResID);
            mContentView = contentStub.inflate();
            Drawable background = mContentView.getBackground();
            if (background != null) {
                mContentView.setBackground(null);
                mView.setBackground(background);
            }
        }
    }

    private void setupRefreshLayout(SmartRefreshLayout smartRefreshLayout) {
        mRefreshLayout = new RefreshLayoutProxy(smartRefreshLayout);
        processRefreshType(mRefreshType);
        mRefreshLayout.setRefreshAndLoadMoreListener(new OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull IRefreshLayout refreshLayout) {
                if (mOnViewStateListener != null) {
                    mOnViewStateListener.onRefresh(refreshLayout);
                }
            }

            @Override
            public void onLoadMore(@NonNull IRefreshLayout refreshLayout) {
                if (mOnViewStateListener != null) {
                    mOnViewStateListener.onLoadMore(refreshLayout);
                }
            }
        });
    }

    private void setRefreshLayoutState(ViewState viewState) {
        if (viewState == ViewState.STATE_COMPLETED) {
            processRefreshType(mRefreshType);
        } else {
            if (mRefreshLayout != null) {
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(false);
            }
        }
    }

    private void processRefreshType(@RefreshType.Val int refreshType) {
        if (mRefreshLayout == null) {
            return;
        }
        switch (refreshType) {
            case RefreshType.REFRESH_AND_LOAD_MORE:
                mRefreshLayout.setEnableRefresh(true);
                mRefreshLayout.setEnableLoadMore(true);
                break;
            case RefreshType.REFRESH_ONLY:
                mRefreshLayout.setEnableRefresh(true);
                mRefreshLayout.setEnableLoadMore(false);
                break;
            case RefreshType.LOAD_MORE_ONLY:
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(true);
                break;
            default:
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(false);
                break;
        }
    }

    View setTitleLayout(boolean isImmersionBarEnable, boolean isContentUnderTitleBar) {
        if (mTitleView != null) {
            return mTitleView;
        }
        setViewStubLayoutRes(R.id.stub_titlebar, mViewConfig.getTitleLayoutId());
        mTitleView = inflateViewStub(R.id.stub_titlebar);
        if (mTitleView instanceof TitleView) {
            ((TitleView) mTitleView).setImmersionBarEnable(isImmersionBarEnable);
        }
        layoutTitleView(isImmersionBarEnable,isContentUnderTitleBar,true);
        return mTitleView;
    }

     void layoutTitleView(boolean isImmersionBarEnable, boolean isContentUnderTitleBar,boolean fromInit) {
        if (mContentWrapView == null) {
            return;
        }
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mContentWrapView
                .getLayoutParams();
        if (isImmersionBarEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen
                        .titlebar_size) +
                        BarUtils.getStatusBarHeight(mContext);
            } else {
                mlp.topMargin = 0;
                mTitleView.bringToFront();
            }

        } else {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen
                        .titlebar_size);
            } else {
                mlp.topMargin = 0;
                mTitleView.bringToFront();
            }
        }
        if(!fromInit){
            mContentWrapView.getParent().requestLayout();
        }
    }

    @Override
    public void setTitleLayoutLayout(int layoutResId) {
        mViewConfig.setTitleLayoutId(layoutResId);
    }

    /**
     * Desc:设置空布局
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param layoutResId
     */
    @Override
    public void setEmptyLayout(@LayoutRes int layoutResId) {
        mViewConfig.setEmptyLayoutId(layoutResId);
    }

    /**
     * Desc:设置加载中布局
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param layoutResId
     */
    @Override
    public void setLoadingLayout(@LayoutRes int layoutResId) {
        mViewConfig.setLoadingLayoutId(layoutResId);
    }

    /**
     * Desc:设置重载布局
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param layoutResId
     */
    @Override
    public void setErrorLayout(@LayoutRes int layoutResId) {
        mViewConfig.setErrorLayoutId(layoutResId);
    }

    /**
     * Desc:设置需要登录布局
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param loginLayoutId
     */
    @Override
    public void setLoginLayoutId(@LayoutRes int loginLayoutId) {
        mViewConfig.setLoginLayoutId(loginLayoutId);
    }

    /**
     * Desc:设置空数据状态页面的文本提示
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param textRes
     */
    @Override
    public void setEmptyText(@StringRes int textRes) {
        mViewConfig.setEmptyTxt(mContext.getString(textRes));
        if (mEmptyTv != null) {
            mEmptyTv.setText(textRes);
        }
    }

    /**
     * Desc:设置空数据状态页面的文本提示
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param text
     */
    @Override
    public void setEmptyText(String text) {
        mViewConfig.setEmptyTxt(text);
        if (mEmptyTv != null) {
            mEmptyTv.setText(text);
        }
    }

    @Override
    public void setEmptyTextColor(@ColorRes int color) {
        mViewConfig.setEmptyTxtColor(color);
        if (mEmptyTv != null) {
            mEmptyTv.setTextColor(ContextCompat.getColor(mContext, color));
        }
    }

    /**
     * Desc:设置空数据状态页面的图标
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param icon
     */
    @Override
    public void setEmptyIcon(@DrawableRes int icon) {
        mViewConfig.setEmptyDrableId(icon);
        if (mEmptyImgv != null) {
            mEmptyImgv.setImageResource(mViewConfig.getEmptyDrableId());
        }
    }

    /**
     * Desc:设置空数据状态页面的按钮文本
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param text
     */
    @Override
    public void setEmptyBtnTxt(String text) {
        mViewConfig.setEmptyBtnTxt(text);
        if (mEmptyBtn != null) {
            mEmptyBtn.setText(mViewConfig.getEmptyBtnTxt());
            mEmptyBtn.setVisibility(View.VISIBLE);
        }
    }

    private void ensureEmptyView() {
        if (mEmptyView == null) {
            setViewStubLayoutRes(R.id.stub_empty, mViewConfig.getEmptyLayoutId());
            mEmptyView = inflateViewStub(R.id.stub_empty);

            mEmptyImgv = (ImageView) mEmptyView.findViewById(R.id.no_data_image);
            mEmptyTv = (TextView) mEmptyView.findViewById(R.id.no_data_txt);
            mEmptyBtn = (TextView) mEmptyView.findViewById(R.id.no_data_button);

            if (mEmptyImgv != null && mViewConfig.getEmptyDrableId() != -1) {
                mEmptyImgv.setImageResource(mViewConfig.getEmptyDrableId());
            }
            if (mEmptyTv != null && !TextUtils.isEmpty(mViewConfig.getEmptyTxt())) {
                mEmptyTv.setText(mViewConfig.getEmptyTxt());
            }
            if (mEmptyTv != null && mViewConfig.getEmptyTxtColor() != 0) {
                mEmptyTv.setTextColor(ContextCompat.getColor(mContext, mViewConfig.getEmptyTxtColor()));
            }
            if (mEmptyBtn != null) {
                if (!TextUtils.isEmpty(mViewConfig.getEmptyBtnTxt())) {
                    mEmptyBtn.setText(mViewConfig.getEmptyBtnTxt());
                    mEmptyBtn.setVisibility(View.VISIBLE);
                }
                mEmptyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnViewStateListener != null) {
                            mOnViewStateListener.onNodataBtnClick(v);
                        }
                    }
                });
            }
        }
    }

    private void ensureLoadingView() {
        if (mLoadingView == null) {
            setViewStubLayoutRes(R.id.stub_loading, mViewConfig.getLoadingLayoutId());
            mLoadingView = inflateViewStub(R.id.stub_loading);
        }
    }

    private void ensureErrorView() {
        if (mErrorView == null) {
            setViewStubLayoutRes(R.id.stub_error, mViewConfig.getErrorLayoutId());
            mErrorView = inflateViewStub(R.id.stub_error);

            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnViewStateListener != null) {
                        mOnViewStateListener.onReload(v);
                    }
                }
            });
            mErrorImgv = (ImageView) mErrorView.findViewById(R.id.imgv_reload);
            mErrorTv = (TextView) mErrorView.findViewById(R.id.tv_reload);
            mRetryTv = (TextView) mErrorView.findViewById(R.id.tv_retry);

        }
    }

    private void ensureLoginView() {
        // TODO: 2018/4/5
        if (mLoginView == null) {
            setViewStubLayoutRes(R.id.stub_login, mViewConfig.getLoginLayoutId());
            mLoginView = inflateViewStub(R.id.stub_login);

            mLoginTv = mLoginView.findViewById(R.id.btn_login_new);
            if (mLoginTv != null) {
                mLoginTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*LoginProxyAction.create(mContext).setLoginCall(new com.hisunflytone.cmdm.module.login.LoginCallback() {
                            @Override
                            public void doAction(boolean hasSigned) {
                                if (!hasSigned) {
                                    if (mOnViewStateListener != null) {
                                        mOnViewStateListener.onAfterLogin();
                                    }
                                }
                            }
                        }).setIsProactiveLogin(true).execute();*/
                    }
                });
            }
        }
    }

    private void setViewStubLayoutRes(@IdRes int stubId, @LayoutRes int layoutResId) {
        ViewStub viewStub = (ViewStub) mView.findViewById(stubId);
        if (viewStub != null) {
            viewStub.setLayoutResource(layoutResId);
        }
    }

    private View inflateViewStub(@IdRes int stubId) {
        ViewStub viewStub = (ViewStub) mView.findViewById(stubId);
        if (viewStub != null && viewStub.getLayoutResource() != 0) {
            return viewStub.inflate();
        }
        return null;
    }


    /**
     * Desc:获取base View
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view
     */
    public View getView() {
        return mView;
    }

    /**
     * Desc:获取标题栏view
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param isImmersionBarEnable
     * @param isContentUnderTitleBar
     * @return title view
     */
    TitleView getTitleView(boolean isImmersionBarEnable, boolean isContentUnderTitleBar) {
        if (mTitleView == null) {
            setTitleLayout(isImmersionBarEnable, isContentUnderTitleBar);
        }
        return (TitleView) mTitleView;
    }

    /**
     * Desc:获取空数据状态view
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view
     */
    @Override
    public View getEmptyView() {
        ensureEmptyView();
        return mEmptyView;
    }

    /**
     * Desc:获取重载状态view
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view
     */
    @Override
    public View getErrorView() {
        ensureErrorView();
        return mErrorView;
    }

    /**
     * Desc:获取内容区域view
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view
     */
    @Override
    public View getContentView() {
        return mContentView;
    }

    /**
     * Desc:获取加载中状态view
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view
     */
    @Override
    public View getLoadingView() {
        ensureLoadingView();
        return mLoadingView;
    }

    /**
     * Desc:获取当前页面状态
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @return view state
     */
    @Override
    public ViewState getViewState() {
        return mCurrentState;
    }

    /**
     * Desc:获取刷新控件
     * <p>
     * Author: jinyuef
     * Date: 2018-04-28
     *
     * @return refresh layout
     */
    @Override
    public IRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    /**
     * Desc:设置页面状态
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param viewState
     * @param show
     * @param hideOther
     * @param args
     */
    void showState(ViewState viewState, boolean show, boolean hideOther, Object... args) {
        setRefreshLayoutState(viewState);
        if (hideOther) {
            setContentViewVisible(ViewState.STATE_COMPLETED == viewState & show);//加载完成
            setLoadingViewVisible(ViewState.STATE_LOADING == viewState & show);//加载中
            setErrorViewVisible(ViewState.STATE_ERROR == viewState & show, args);//加载失败
            setEmptyViewVisible(ViewState.STATE_EMPTY == viewState & show, args);//空数据
            setLoginViewVisible(ViewState.STATE_LOGIN == viewState & show);//需要登录
        } else {
            switch (viewState) {
                case STATE_COMPLETED://加载完成
                    setContentViewVisible(show);
                    break;
                case STATE_LOADING://加载中
                    setLoadingViewVisible(show);
                    break;
                case STATE_ERROR://加载失败
                    setErrorViewVisible(show, args);
                    break;
                case STATE_EMPTY://空数据
                    setEmptyViewVisible(show, args);
                    break;
                case STATE_LOGIN://需要登录
                    setLoginViewVisible(show);
                    break;
            }
        }
        mCurrentState = viewState;
    }

    private void setContentViewVisible(boolean visible) {
        if (visible && mContentView == null) {
            mContentView = inflateViewStub(R.id.stub_content);
        }
        if (mContentView != null) {
            mContentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setEmptyViewVisible(boolean visible, Object... args) {
        if (visible && mEmptyView == null) {
            ensureEmptyView();
        }
        if (mEmptyView != null) {
            if (args != null) {
                //setEmptyArgs(args);
                for (Object arg : args) {
                    if (arg instanceof Integer) {
                        int resId = (int) arg;
                        String typeName = mContext.getResources().getResourceTypeName(resId);
                        if ("string".equals(typeName)) {
                            mEmptyTv.setText(resId);
                        }

                    } else if (arg instanceof CharSequence) {
                        mEmptyTv.setText((CharSequence) arg);
                    }
                }
            }
            mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setLoadingViewVisible(final boolean visible) {
        if (visible && mLoadingView == null) {
            ensureLoadingView();
        }
        if (mLoadingView != null) {
            mLoadingView.post(new Runnable() {
                @Override
                public void run() {
                    mLoadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });

        }
    }

    private void setErrorViewVisible(boolean visible, Object... args) {
        if (visible && mErrorView == null) {
            ensureErrorView();
        }
        if (mErrorView != null) {
            setErrorArgs(args);
            mErrorView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setLoginViewVisible(boolean visible) {
        if (visible && mLoginView == null) {
            ensureLoginView();
        }
        if (mLoginView != null) {
            mLoginView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Desc:设置重载页面文本提示及图标
     * <p>
     * Author: jinyuef
     * Date: 2018-04-25
     *
     * @param args
     */
    public void setErrorArgs(Object... args) {
        if (mErrorImgv == null) {
            ensureEmptyView();
        }
        if (mErrorImgv == null || mErrorTv == null) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof Integer) {
                int resId = (int) arg;
                String typeName = mContext.getResources().getResourceTypeName(resId);
                if ("string".equals(typeName)) {
                    mErrorTv.setText(resId);
                } else {
                    mErrorImgv.setImageResource(resId);
                }

            } else if (arg instanceof CharSequence) {
                mErrorTv.setText((CharSequence) arg);
            }
        }
    }
}
