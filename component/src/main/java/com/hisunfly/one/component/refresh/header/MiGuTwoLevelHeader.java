package com.hisunfly.one.component.refresh.header;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hisunfly.one.component.R;
import com.hisunfly.one.component.utils.DensityUtils;
import com.hisunfly.one.component.utils.PrintLog;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshInternal;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * description:
 *
 * @author: 罗明颖
 * @Create Time:2018/7/26
 */

public class MiGuTwoLevelHeader  extends InternalAbstract implements RefreshInternal {
    //<editor-fold desc="属性字段">
    protected int mSpinner;
    protected float mPercent = 0;
    protected float mMaxRage = 3.5f;
    protected float mFloorRage = 3f;
    protected float mRefreshRage = 1f;
    /**
     * 二楼自动返回距离比（与刷新高度比例）
     */
    protected float mFloorAutoReleaseRage = 3.5f;
    protected boolean mEnableTwoLevel = true;
    protected boolean mEnablePullToCloseTwoLevel = true;
    protected int mFloorDuration = 1000;
    protected int mHeaderHeight;
    protected RefreshHeader mRefreshHeader;
    protected RefreshKernel mRefreshKernel;
    /**
     * 二楼回调监听
     */
    protected OnTwoLevelListener mTwoLevelListener;

    /**
     * 刷新控件高度
     */
    private int mSmartRefreshHeight = -1;

    /**
     * 二楼view高度
     */
    private int mRfreshTwoFloorHeight = -1;
    /**
     * 二楼view
     */
    protected ImageView mRefreshTwoFloor;
    /**
     * 二楼view依赖的资源图片
     */
    int[] pullRes = new int[]{
            R.drawable.common_anime_refresh_head_0,
            R.drawable.common_anime_refresh_head_1,
            R.drawable.common_anime_refresh_head_2,
            R.drawable.common_anime_refresh_head_3,
            R.drawable.common_anime_refresh_head_4,
            R.drawable.common_anime_refresh_head_5,
            R.drawable.common_anime_refresh_head_6,
            R.drawable.common_anime_refresh_head_7,
            R.drawable.common_anime_refresh_head_8,
            R.drawable.common_anime_refresh_head_9,
            R.drawable.common_anime_refresh_head_10,
            R.drawable.common_anime_refresh_head_11,
            R.drawable.common_anime_refresh_head_12,
            R.drawable.common_anime_refresh_head_13,};
    /**
     * 手势拖拽时对应的图片索引
     */
    private int mLastIndex;
    /**
     * 刷新动画observable
     */
    private Disposable mRefreshingAnimation;

    //<editor-fold desc="构造方法">
    public MiGuTwoLevelHeader(@NonNull Context context) {
        this(context, null);
    }

    public MiGuTwoLevelHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiGuTwoLevelHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSpinnerStyle = SpinnerStyle.FixedBehind;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MiGuTwoLevelHeader);

        mMaxRage = ta.getFloat(R.styleable.MiGuTwoLevelHeader_srlMaxRage, mMaxRage);
        mFloorRage = ta.getFloat(R.styleable.MiGuTwoLevelHeader_srlFloorRage, mFloorRage);
        mRefreshRage = ta.getFloat(R.styleable.MiGuTwoLevelHeader_srlRefreshRage, mRefreshRage);
        mFloorDuration = ta.getInt(R.styleable.MiGuTwoLevelHeader_srlFloorDuration, mFloorDuration);
        mEnableTwoLevel = ta.getBoolean(R.styleable.MiGuTwoLevelHeader_srlEnableTwoLevel, mEnableTwoLevel);
        mEnablePullToCloseTwoLevel = ta.getBoolean(R.styleable.MiGuTwoLevelHeader_srlEnablePullToCloseTwoLevel, mEnablePullToCloseTwoLevel);

        ta.recycle();
        initTwoFloorView();
    }

    /**
     * desc:初始化二层view
     * <p>
     * author:罗明颖
     * date:2018/7/26
     */
    private void initTwoFloorView() {
        mRefreshTwoFloor = new ImageView(getContext());
        mRefreshTwoFloor.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT,DensityUtils.dp2px(getContext(), 420));
        layoutParams.setMargins(DensityUtils.dp2px(getContext(), 60), 0, 0, 0);
        addView(mRefreshTwoFloor,layoutParams );
    }

    //</editor-fold>

    //<editor-fold desc="生命周期">
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final ViewGroup thisGroup = this;
        for (int i = 0, len = thisGroup.getChildCount(); i < len; i++) {
            View childAt = thisGroup.getChildAt(i);
            if (childAt instanceof RefreshHeader) {
                mRefreshHeader = (RefreshHeader) childAt;
                mWrappedInternal = (RefreshInternal) childAt;
                thisGroup.bringChildToFront(childAt);
                break;
            }
        }
        if (mRefreshHeader == null) {
            final ViewGroup thisView = this;
            setRefreshHeader(new ClassicsHeader(thisView.getContext()));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSpinnerStyle = SpinnerStyle.MatchLayout;
        if (mRefreshHeader == null) {
            final ViewGroup thisView = this;
            setRefreshHeader(new ClassicsHeader(thisView.getContext()));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSpinnerStyle = SpinnerStyle.FixedBehind;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final RefreshHeader refreshHeader = mRefreshHeader;
        if (refreshHeader != null) {
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            if (mode == MeasureSpec.AT_MOST) {
                refreshHeader.getView().measure(widthMeasureSpec, heightMeasureSpec);
                int height = refreshHeader.getView().getMeasuredHeight();
                super.setMeasuredDimension(View.resolveSize(super.getSuggestedMinimumWidth(), widthMeasureSpec), height);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean equals(Object obj) {
        final Object header = mRefreshHeader;
        return (header != null && header.equals(obj)) || super.equals(obj);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        final View thisView = this;
        final RefreshHeader refreshHeader =  mRefreshHeader;
        if (refreshHeader == null) {
            return;
        }
        if (1f * (maxDragHeight + height) / height != mMaxRage && mHeaderHeight == 0) {
            mHeaderHeight = height;
            mRefreshHeader = null;
            kernel.getRefreshLayout().setHeaderMaxDragRate(mMaxRage);
            mRefreshHeader = refreshHeader;
        }
        if (mRefreshKernel == null //第一次初始化
                && refreshHeader.getSpinnerStyle() == SpinnerStyle.Translate
                && !thisView.isInEditMode()) {
            MarginLayoutParams params = (MarginLayoutParams) refreshHeader.getView().getLayoutParams();
            params.topMargin -= height;
            refreshHeader.getView().setLayoutParams(params);
        }

        mHeaderHeight = height;
        mRefreshKernel = kernel;
        kernel.requestFloorDuration(mFloorDuration);
        kernel.requestNeedTouchEventFor(this, !mEnablePullToCloseTwoLevel);
        refreshHeader.onInitialized(kernel, height, maxDragHeight);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final RefreshHeader refreshHeader = mRefreshHeader;
        if (refreshHeader != null) {
            refreshHeader.onStateChanged(refreshLayout, oldState, newState);
            switch (newState) {
                case Refreshing:
                    startRefreshingAnimation();
                    PrintLog.d("MuGuTwoHeader","Refreshing");
                    break;
                case RefreshFinish:
                    stopRefreshingAnimation();
                    PrintLog.d("MuGuTwoHeader","RefreshFinish");
                    break;
                case TwoLevelReleased:
                    if (refreshHeader.getView() != this) {
                        refreshHeader.getView().animate().alpha(0).setDuration(mFloorDuration / 2);
                    }
                    final RefreshKernel refreshKernel = mRefreshKernel;
                    if (refreshKernel != null) {
                        final OnTwoLevelListener twoLevelListener = mTwoLevelListener;
                        //隐藏二楼view
                        mRefreshTwoFloor.setVisibility(View.INVISIBLE);
                        refreshKernel.startTwoLevel(twoLevelListener == null || twoLevelListener.onTwoLevel(refreshLayout));
                    }
                    break;
                case TwoLevel:
                    break;
                case TwoLevelFinish:
                    if (refreshHeader.getView() != this) {
                        refreshHeader.getView().animate().alpha(1).setDuration(mFloorDuration / 2);
                    }
                    break;
                case PullDownToRefresh:
                    if (refreshHeader.getView().getAlpha() == 0 && refreshHeader.getView() != this) {
                        refreshHeader.getView().setAlpha(1);
                    }
                    break;
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        moveSpinner(offset);
        changeFloorImageSource(percent, offset);
        final RefreshHeader refreshHeader = mRefreshHeader;
        final RefreshKernel refreshKernel = mRefreshKernel;
        if (refreshHeader != null) {
            refreshHeader.onMoving(isDragging, percent, offset, height, maxDragHeight);
        }
        if (isDragging) {
//            if(mPercent < mFloorAutoReleaseRage && percent >= mFloorAutoReleaseRage && mEnableTwoLevel){
//                refreshKernel.setState(RefreshState.TwoLevelReleased);
//            } else
            if (mPercent < mFloorRage && percent >= mFloorRage && mEnableTwoLevel) {
                refreshKernel.setState(RefreshState.ReleaseToTwoLevel);
            } else if (mPercent >= mFloorRage && percent < mRefreshRage) {
                refreshKernel.setState(RefreshState.PullDownToRefresh);
            } else if (mPercent >= mFloorRage && percent < mFloorRage) {
                refreshKernel.setState(RefreshState.ReleaseToRefresh);
            }
            mPercent = percent;
        }
    }

    protected void moveSpinner(int spinner) {
        final RefreshHeader refreshHeader = mRefreshHeader;
        if (mSpinner != spinner && refreshHeader != null) {
            mSpinner = spinner;
            switch (refreshHeader.getSpinnerStyle()) {
                case Translate:
                    refreshHeader.getView().setTranslationY(spinner);
                    break;
                case Scale:{
                    View view = refreshHeader.getView();
                    view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getTop() + Math.max(0, spinner));
                    break;
                }
            }
        }
    }

    /**
     * desc:改变floor的图片，形成帧动画效果
     * <p>
     * author:罗明颖
     * date:2018/7/26
     */
    private void changeFloorImageSource(float percent,int offset){
        int p = (int) (percent * 10);
        int index = p % pullRes.length;
        //手势拖拽时 对应的图片资源索引
        mLastIndex = index;
        //设置对应下拉图片，
        mRefreshTwoFloor.setImageResource(pullRes[index]);
        //依据移动距离 ， 移动二层view
        ViewParent parent = getParent();
        if (mSmartRefreshHeight == -1 && parent instanceof SmartRefreshLayout) {
            mSmartRefreshHeight = ((SmartRefreshLayout) parent).getHeight();
        }
        if (mRfreshTwoFloorHeight == -1 && mRefreshTwoFloor != null) {
            mRfreshTwoFloorHeight = mRefreshTwoFloor.getHeight();
        }
        mRefreshTwoFloor.setTranslationY(Math.min(offset - mRfreshTwoFloorHeight, mSmartRefreshHeight - mRfreshTwoFloorHeight));
    }

    /**
     * 设置指定的Header
     */
    public MiGuTwoLevelHeader setRefreshHeader(RefreshHeader header) {
        return setRefreshHeader(header, MATCH_PARENT, WRAP_CONTENT);
    }

    /**
     * 设置指定的Header
     */
    public MiGuTwoLevelHeader setRefreshHeader(RefreshHeader header, int width, int height) {
        final ViewGroup thisGroup = this;
        if (header != null) {
            final RefreshHeader refreshHeader = mRefreshHeader;
            if (refreshHeader != null) {
                thisGroup.removeView(refreshHeader.getView());
            }
            if (header.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                thisGroup.addView(header.getView(), 0, new LayoutParams(width, height));
            } else {
                thisGroup.addView(header.getView(), width, height);
            }
//            this.mWrappedView = header.getView();
            this.mRefreshHeader = header;
            this.mWrappedInternal = header;
        }
        return this;
    }

    /**
     * 设置下拉Header的最大高度比值
     * @param rate MaxDragHeight/HeaderHeight
     */
    public MiGuTwoLevelHeader setMaxRage(float rate) {
        if (this.mMaxRage != rate) {
            this.mMaxRage = rate;
            final RefreshKernel refreshKernel = mRefreshKernel;
            if (refreshKernel != null) {
                this.mHeaderHeight = 0;
                refreshKernel.getRefreshLayout().setHeaderMaxDragRate(mMaxRage);
            }
        }
        return this;
    }

    /**
     * desc:开始刷新动画
     * <p>
     * author:罗明颖
     * date:2018/8/7
     */
    private void startRefreshingAnimation() {
        stopRefreshingAnimation();
        //索引递增
        mRefreshingAnimation = Observable.interval(60, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        animateRefreshing(mLastIndex);
                        //索引递增
                        mLastIndex++;
                    }
                });
    }

    /**
     * desc:执行刷新动画
     * <p>
     * author:罗明颖
     * date:2018/8/7
     */
    private void animateRefreshing(int index) {
        if (mRefreshTwoFloor != null) {
            index = index % pullRes.length;
            mRefreshTwoFloor.setImageResource(pullRes[index]);
        }
    }

    /**
     * desc:停止刷新动画
     * <p>
     * author:罗明颖
     * date:2018/8/7
     */
    private void stopRefreshingAnimation() {
        if (mRefreshingAnimation != null && !mRefreshingAnimation.isDisposed()) {
            mRefreshingAnimation.dispose();
        }
    }

    /**
     * 是否禁止在二极状态是上滑关闭状态回到初态
     * @param enabled 是否启用
     */
    public MiGuTwoLevelHeader setEnablePullToCloseTwoLevel(boolean enabled) {
        final RefreshKernel refreshKernel = mRefreshKernel;
        this.mEnablePullToCloseTwoLevel = enabled;
        if (refreshKernel != null) {
            refreshKernel.requestNeedTouchEventFor(this, !enabled);
//            refreshKernel.requestNeedTouchEventWhenRefreshing(disable);
        }
        return this;
    }

    public MiGuTwoLevelHeader setFloorRage(float rate) {
        this.mFloorRage = rate;
        return this;
    }

    public MiGuTwoLevelHeader setRefreshRage(float rate) {
        this.mRefreshRage = rate;
        return this;
    }

    public MiGuTwoLevelHeader setEnableTwoLevel(boolean enabled) {
        this.mEnableTwoLevel = enabled;
        return this;
    }

    public MiGuTwoLevelHeader setFloorDuration(int duration) {
        this.mFloorDuration = duration;
        return this;
    }

    public MiGuTwoLevelHeader setOnTwoLevelListener(OnTwoLevelListener listener) {
        this.mTwoLevelListener = listener;
        return this;
    }

    public MiGuTwoLevelHeader finishTwoLevel() {
        if (mRefreshTwoFloor != null) {
            mRefreshTwoFloor.setVisibility(View.VISIBLE);
        }
        final RefreshKernel refreshKernel = mRefreshKernel;
        if (refreshKernel != null) {
            refreshKernel.finishTwoLevel();
        }
        return this;
    }

    //</editor-fold>
}
