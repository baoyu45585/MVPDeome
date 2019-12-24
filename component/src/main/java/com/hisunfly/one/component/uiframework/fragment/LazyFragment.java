//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class LazyFragment extends BaseFragment0 {
    protected String TAG = this.getClass().getSimpleName();
    protected boolean isInit = false;
    private Bundle savedInstanceState;
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
    private boolean isLazyLoad = true;
    private FrameLayout layout;
    private int isVisibleToUserState = -1;
    private static final int VISIBLE_STATE_NOTSET = -1;
    private static final int VISIBLE_STATE_VISIABLE = 1;
    private static final int VISIBLE_STATE_GONE = 0;
    private boolean isStart = false;

    public LazyFragment() {
    }

    /** @deprecated */
    @Deprecated
    protected final void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.isLazyLoad = bundle.getBoolean("intent_boolean_lazyLoad", this.isLazyLoad);
        }

        boolean isVisibleToUser;
        if (this.isVisibleToUserState == -1) {
            isVisibleToUser = this.getUserVisibleHint();
        } else {
            isVisibleToUser = this.isVisibleToUserState == 1;
        }

        if (this.isLazyLoad) {
            if (isVisibleToUser && !this.isInit) {
                this.isInit = true;
                this.onCreateViewLazy(savedInstanceState);
            } else {
                LayoutInflater layoutInflater = this.inflater;
                if (layoutInflater == null) {
                    layoutInflater = LayoutInflater.from(this.getApplicationContext());
                }

                this.layout = new FrameLayout(layoutInflater.getContext());
                View view = this.getPreviewLayout(layoutInflater, this.layout);
                if (view != null) {
                    this.layout.addView(view);
                }

                this.layout.setLayoutParams(new LayoutParams(-1, -1));
                super.setContentView(this.layout);
                if (savedInstanceState != null) {
                    boolean isFromSaveInstanceState = savedInstanceState.getBoolean("isFromSaveInstanceState", false);
                    if (isFromSaveInstanceState) {
                        this.isInit = true;
                        this.savedInstanceState = savedInstanceState;
                        this.onCreateViewLazy(savedInstanceState);
                    }
                }
            }
        } else {
            this.isInit = true;
            this.onCreateViewLazy(savedInstanceState);
        }

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUserState = isVisibleToUser ? 1 : 0;
        if (isVisibleToUser && !this.isInit && this.getContentView0() != null) {
            this.isInit = true;
            this.onCreateViewLazy(this.savedInstanceState);
            this.onResumeLazy();
        }

        if (this.isInit && this.getContentView0() != null) {
            if (isVisibleToUser) {
                this.isStart = true;
                this.onFragmentStartLazy0();
                this.onFragmentStartLazy();
            } else {
                this.isStart = false;
                this.onFragmentStopLazy();
            }
        }

    }

    public boolean getUserVisibleHint() {
        if (this.isVisibleToUserState == -1) {
            return super.getUserVisibleHint();
        } else {
            return this.isVisibleToUserState == 1;
        }
    }

    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    /** @deprecated */
    @Deprecated
    public final void onStart() {
        super.onStart();
        if (this.isInit && !this.isStart && this.getUserVisibleHint()) {
            this.isStart = true;
            this.onFragmentStartLazy0();
            this.onFragmentStartLazy();
        }

    }

    /** @deprecated */
    @Deprecated
    public final void onStop() {
        super.onStop();
        if (this.isInit && this.isStart && this.getUserVisibleHint()) {
            this.isStart = false;
            this.onFragmentStopLazy();
        }

    }

    protected void onFragmentStartLazy0() {
    }

    protected void onFragmentStartLazy() {
    }

    protected void onFragmentStopLazy() {
    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {
    }

    protected void onResumeLazy() {
    }

    protected void onPauseLazy() {
    }

    protected void onDestroyViewLazy() {
    }

    public void setContentView(int layoutResID) {
        if (this.isLazyLoad && this.getContentView0() != null && this.getContentView0().getParent() != null) {
            this.layout.removeAllViews();
            View view = this.inflater.inflate(layoutResID, this.layout, false);
            this.layout.addView(view);
        } else {
            super.setContentView(layoutResID);
        }

    }

    public void setContentView(View view) {
        if (this.isLazyLoad && this.getContentView0() != null && this.getContentView0().getParent() != null) {
            this.layout.removeAllViews();
            this.layout.addView(view);
        } else {
            super.setContentView(view);
        }

    }

    /** @deprecated */
    @Deprecated
    public final void onResume() {
        super.onResume();
        if (this.isInit) {
            this.onResumeLazy();
        }

    }

    /** @deprecated */
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (this.isInit) {
            this.onPauseLazy();
        }

    }

    /** @deprecated */
    @Deprecated
    public final void onDestroyView() {
        super.onDestroyView();
        if (this.isInit) {
            this.onDestroyViewLazy();
        }

        this.isInit = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFromSaveInstanceState", true);
        super.onSaveInstanceState(outState);
    }
}
