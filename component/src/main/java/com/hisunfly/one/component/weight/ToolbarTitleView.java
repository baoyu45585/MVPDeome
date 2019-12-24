package com.hisunfly.one.component.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hisunfly.one.component.R;
import com.hisunfly.one.component.uiframework.inface.TitleView;
import com.hisunfly.one.component.utils.BarUtils;
import com.hisunfly.one.component.utils.DensityUtils;



/**
 * desc:通用标题栏
 * <p/>
 * author: jinyuef
 * date: 2018/4/5
 */
public class ToolbarTitleView extends LinearLayout implements TitleView {

    private RelativeLayout mTopBarLayout;

    private LinearLayout mToolbarLeftLayout;
    private ImageView mToolbarLeftImgv;
    private TextView mToolbarLeftTv;

    private LinearLayout mToolbarRightLayout;

    private TextView mTitleTv;

    public ToolbarTitleView(Context context) {
        super(context);
    }

    public ToolbarTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopBarLayout = (RelativeLayout) findViewById(R.id.top_bar_layout);

        mToolbarLeftLayout = (LinearLayout) findViewById(R.id.toolbar_left_layout);
        mToolbarLeftImgv = (ImageView) findViewById(R.id.toolbar_left_imgv);
        mToolbarLeftTv = (TextView) findViewById(R.id.toolbar_left_tv);

        mToolbarRightLayout = findViewById(R.id.toolbar_right_layout);

        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setSelected(true);
    }

    @Override
    public void setImmersionBarEnable(boolean isImmersionBarEnable) {
        try {
            if (isImmersionBarEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                LayoutParams lp = (LayoutParams) mTopBarLayout.getLayoutParams();
                lp.topMargin = BarUtils.getStatusBarHeight(getContext());
            } else {
                LayoutParams lp = (LayoutParams) mTopBarLayout.getLayoutParams();
                lp.topMargin = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ToolbarTitleView setNavIconOnClickListener(OnClickListener onClickListener) {
        mToolbarLeftImgv.setOnClickListener(onClickListener);
        return this;
    }

    @Override
    public ToolbarTitleView setNavIcon(@Nullable Drawable icon) {
        mToolbarLeftImgv.setImageDrawable(icon);
        return this;
    }

    @Override
    public ToolbarTitleView setNavIcon(@DrawableRes int resId) {
        mToolbarLeftImgv.setImageResource(resId);
        return this;
    }

    @Override
    public ToolbarTitleView setNavIconVisibility(boolean isVisible) {
        mToolbarLeftImgv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public ToolbarTitleView setLeftText(String leftTxt, OnClickListener onClickListener) {
        mToolbarLeftTv.setVisibility(View.VISIBLE);
        mToolbarLeftTv.setText(leftTxt);
        mToolbarLeftTv.setOnClickListener(onClickListener);
        return this;
    }

    @Override
    public ToolbarTitleView setLeftText(String leftTxt) {
        return setLeftText(leftTxt, null);
    }

    @Override
    public ToolbarTitleView setLeftTextColor(@ColorInt int color) {
        mToolbarLeftTv.setTextColor(color);
        return this;
    }

    @Override
    public ToolbarTitleView setLeftTextSize(int sizeOfSp) {
        mToolbarLeftTv.setTextSize(sizeOfSp);
        return this;
    }

    @Override
    public TextView getLeftText() {
        return mToolbarLeftTv;
    }

    @Override
    public ToolbarTitleView setTitleText(CharSequence title) {
        mTitleTv.setText(title);
        return this;
    }

    @Override
    public ToolbarTitleView setTitleText(@StringRes int title) {
        mTitleTv.setText(title);
        return this;
    }

    @Override
    public ToolbarTitleView setTitleTextColor(@ColorInt int color) {
        mTitleTv.setTextColor(color);
        return this;
    }

    @Override
    public ToolbarTitleView setTitleTextSize(int sizeOfSp) {
        mTitleTv.setTextSize(sizeOfSp);
        return this;
    }

    @Override
    public ToolbarTitleView setTitleGravityLeft() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleTv.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, R.id.toolbar_left_layout);
        params.addRule(RelativeLayout.LEFT_OF, R.id.toolbar_right_layout);
        return this;
    }

    @Override
    public TextView getTitleText() {
        return mTitleTv;
    }

    @Override
    public Toolbar getToolbar() {
        //用于后期扩展，暂时没用
        return null;
    }

    @Override
    public <B extends View> B setCenterView(@LayoutRes int layoutId) {
        ViewStub viewStub = (ViewStub) findViewById(R.id.stub_center);
        if (viewStub != null) {
            viewStub.setLayoutResource(layoutId);
            return (B) viewStub.inflate();
        }
        return null;
    }

    @Override
    public <B extends View> B setLayoutView(int layoutId) {
        ViewStub viewStub = (ViewStub) findViewById(R.id.stub_layout);
        if (viewStub != null) {
            viewStub.setLayoutResource(layoutId);
            return (B) viewStub.inflate();
        }
        return null;
    }

    @Override
    public ToolbarTitleView addMenuItem(View view, OnClickListener onClickListener) {
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        param.rightMargin = DensityUtils.dp2px(getContext(), 12);
        view.setOnClickListener(onClickListener);
        mToolbarRightLayout.addView(view, param);
        return this;
    }

    @Override
    public ToolbarTitleView addMenuItem(ViewGroup viewGroup) {
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mToolbarRightLayout.addView(viewGroup, param);
        return this;
    }

    @Override
    public TextView addMenuItem(String text, OnClickListener onClickListener) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.top_bar_btn, null);
        TextView btnTxt = (TextView) root.findViewById(R.id.text);
        btnTxt.setTextColor(getResources().getColor(R.color.white));
        btnTxt.setTag(getContext().getString(R.string.component_dispatch));
        btnTxt.setText(text);
        this.addMenuItem(root, onClickListener);
        return btnTxt;
    }

    @Override
    public void clearMenuItems() {
        mToolbarRightLayout.removeAllViews();
    }
}
