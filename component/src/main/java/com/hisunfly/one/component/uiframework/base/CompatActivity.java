//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hisunfly.one.component.R;
import com.hisunfly.one.component.uiframework.mvp.RxMvpView;
import com.hisunfly.one.component.utils.BarUtils;
import com.hisunfly.one.component.utils.EventBusUtils;
import com.hisunfly.one.component.utils.NetworkUtils;
import com.hisunfly.one.component.utils.PrintLog;
import com.hisunfly.one.component.utils.SoftKeyboardUtil;
import com.hisunfly.one.component.utils.UiHandler;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CompatActivity extends RxAppCompatActivity implements RxMvpView {
    protected String TAG = this.getClass().getSimpleName();
    protected Unbinder mUnbinder;
    protected ImmersionBar mImmersionBar;
    private boolean mKeyboardOpened;
    protected Handler mUIHandler;
    private boolean mEnableNetworkMonitor;
    private NetworkUtils.NetworkListener mNetworkListener;
    private boolean mNetworkAvail;
    private CompatActivity.MyBroadcastReceiver mMyBroadcastReceiver;
    protected Activity mActivity;
    protected FragmentManager fragmentManager;

    public CompatActivity() {
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.mActivity = this;
        this.init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.fragmentManager = this.getSupportFragmentManager();
        if (this.isFinalImmersionBarEnable()) {
            this.initImmersionBar();
        }

        try {
            this.onBeforeInit(savedInstanceState);
            this.setContainerView();
            if (this.isEnableButterKnife()) {
                this.mUnbinder = ButterKnife.bind(this);
            }

            this.initLayout();
            this.setListener();
            this.initData();
            this.initNetworkMonitor();
            if (this.isApplyEventBus()) {
                EventBusUtils.register(this);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var3;
            }
        }

    }

    private void initImmersionBar() {
        this.mImmersionBar = ImmersionBar.with(this).keyboardEnable(this.isNeedHandleSoftKeyboard());
        if (this.isHideStatusBar()) {
            this.mImmersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR);
        }

        try {
            this.mImmersionBar.init();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void init_(Bundle savedInstanceState) {
        this.onBeforeinit0(savedInstanceState);
//        this.afterInject(savedInstanceState);
    }

//    protected void afterInject(Bundle savedInstanceState) {
//        try {
//            this.initComponent(savedInstanceState);
//        } catch (Exception var3) {
//            var3.printStackTrace();
//        }
//
//    }

    protected void onBeforeinit0(Bundle savedInstanceState) {
    }

//    protected abstract void initComponent(Bundle var1);

    protected void onBeforeInit(Bundle savedInstanceState) {
    }

    protected abstract void setContainerView();

    protected abstract void initLayout();

    protected void setListener() {
    }

    protected abstract void initData();

    protected abstract int getLayout();

    protected boolean isImmersionBarEnable() {
        return true;
    }

    protected final boolean isFinalImmersionBarEnable() {
        return this.isImmersionBarEnable() && VERSION.SDK_INT >= 19;
    }

    protected boolean isNeedHandleSoftKeyboard() {
        return false;
    }

    protected boolean isHideStatusBar() {
        return false;
    }

    protected boolean isEnableButterKnife() {
        return true;
    }

    protected void processViewForImmersionBarEnable(@IdRes int resId) {
        try {
            this.processViewForImmersionBarEnable(this.findViewById(resId));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void processViewForImmersionBarEnable(View targetView) {
        try {
            if (this.isFinalImmersionBarEnable()) {
                MarginLayoutParams mLp = (MarginLayoutParams)targetView.getLayoutParams();
                mLp.topMargin = BarUtils.getStatusBarHeight(this.getContext());
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void ensureLocalBroadcast(IntentFilter intentFilter) {
        this.unregisterLocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mMyBroadcastReceiver = new CompatActivity.MyBroadcastReceiver(), intentFilter);
    }

    private void unregisterLocalBroadcastReceiver() {
        if (this.mMyBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mMyBroadcastReceiver);
        }

    }

    protected void processLocalBroadcastReceive(Intent intent) {
    }

    protected void onDestroy() {
        try {
            if (this.mUnbinder != null) {
                this.mUnbinder.unbind();
            }

            this.removeAllPost();
            this.releaseData();
            if (this.isApplyEventBus()) {
                EventBusUtils.unregister(this);
            }

            if (this.mImmersionBar != null) {
                this.mImmersionBar.destroy();
            }

            this.unregisterLocalBroadcastReceiver();
            this.releaseNetworkMonitor();
        } catch (Exception var5) {
            var5.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var5;
            }
        } finally {
            super.onDestroy();
        }

    }

    protected void releaseData() {
    }

    protected abstract View getContentView();

    protected View getRootView() {
        return this.getContentView();
    }

    public boolean isKeyboardOpened() {
        return this.mKeyboardOpened;
    }

    protected void enableKeyboardVisibilityHandle() {
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            public void onVisibilityChanged(boolean isOpen) {
                CompatActivity.this.mKeyboardOpened = isOpen;
                CompatActivity.this.onKeyboardVisibilityChanged(isOpen);
                List<Fragment> fragments = CompatActivity.this.getSupportFragmentManager().getFragments();
                if (fragments != null && fragments.size() > 0) {
                    Iterator var3 = fragments.iterator();

                    while(var3.hasNext()) {
                        Fragment f = (Fragment)var3.next();
                        if (f instanceof CompatLazyFragment) {
                            CompatLazyFragment basex = (CompatLazyFragment)f;
                            if (basex.isHandleKeyboardVisible()) {
                                basex.onKeyboardVisibilityChanged(isOpen);
                            }
                        }

                        if (f instanceof CompatFragment) {
                            CompatFragment base = (CompatFragment)f;
                            if (base.isHandleKeyboardVisible()) {
                                base.onKeyboardVisibilityChanged(isOpen);
                            }
                        }
                    }
                }

            }
        });
    }

    protected void onKeyboardVisibilityChanged(boolean visibility) {
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.mKeyboardOpened && ev.getAction() == 0) {
            View v = this.getCurrentFocus();
            if (this.dispatchClickView((ViewGroup)this.getRootView(), ev)) {
                return super.dispatchTouchEvent(ev);
            }

            if (v != null) {
                View parent = (View)v.getParent();
                if (this.isViewDispatch(parent)) {
                    v = parent;
                }
            }

            if (this.isShouldHideKeyboard(v, ev)) {
                SoftKeyboardUtil.hideKeyboard(this);
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isViewDispatch(View targetView) {
        boolean dispatch = false;
        String componentDispatchStr = this.getString(R.string.component_dispatch);
        if (componentDispatchStr.equals(targetView.getTag()) || componentDispatchStr.equals(targetView.getTag(R.id.component_touch_dispatch))) {
            dispatch = true;
        }

        return dispatch;
    }

    private boolean dispatchClickView(ViewGroup contentView, MotionEvent event) {
        boolean isDispatch = false;
        if (contentView == null) {
            return isDispatch;
        } else {
            for(int i = contentView.getChildCount() - 1; i >= 0; --i) {
                View childView = contentView.getChildAt(i);
                boolean touchView = this.isTouch(event, childView);
                if (childView.isShown()) {
                    if (touchView && this.isViewDispatch(childView)) {
                        isDispatch = true;
                        break;
                    }

                    if (childView instanceof ViewGroup) {
                        ViewGroup itemView = (ViewGroup)childView;
                        if (touchView) {
                            isDispatch |= this.dispatchClickView(itemView, event);
                            break;
                        }
                    }
                }
            }

            return isDispatch;
        }
    }

    private boolean isTouch(MotionEvent event, View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.contains((int)event.getX(), (int)event.getY());
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            return !rect.contains((int)event.getX(), (int)event.getY());
        } else {
            return false;
        }
    }

    public void onBackPressed() {
        boolean fragmentHandled = false;
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            Iterator var3 = fragments.iterator();

            while(var3.hasNext()) {
                Fragment fragment = (Fragment)var3.next();
                if (fragment instanceof CompatLazyFragment) {
                    CompatLazyFragment bf = (CompatLazyFragment)fragment;
                    if (bf.onBackPressed()) {
                        fragmentHandled = true;
                        break;
                    }
                } else if (fragment instanceof CompatFragment) {
                    CompatFragment bf = (CompatFragment)fragment;
                    if (bf.onBackPressed()) {
                        fragmentHandled = true;
                        break;
                    }
                }
            }
        }

        if (!fragmentHandled) {
            ActivityCompat.finishAfterTransition(this);
        }

    }

    public void showFragment(int container, Fragment fragment) {
        this.showFragment(container, fragment, false);
    }

    public void showFragment(int fragmentId) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(fragmentId);
        if (fragment != null) {
            this.showFragment(fragment);
        }

    }

    public void showFragment(Fragment fragment) {
        this.showFragment(fragment, false);
    }

    public void showFragment(Fragment fragment, boolean hideOther) {
        this.showFragment(16908290, fragment, hideOther);
    }

    public void showFragment(int container, Fragment fragment, boolean hideOther) {
        if (fragment.isAdded()) {
            this.getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        } else {
            this.getSupportFragmentManager().beginTransaction().add(container, fragment, fragment.getClass().getName()).commitAllowingStateLoss();
        }

        if (hideOther) {
            List<Fragment> fragmentList = this.getSupportFragmentManager().getFragments();
            if (fragmentList == null) {
                return;
            }

            for(int i = fragmentList.size() - 1; i >= 0; --i) {
                Fragment frag = (Fragment)fragmentList.get(i);
                if (frag != null && !frag.getClass().getName().equals(fragment.getClass().getName())) {
                    Object obj = frag.getTag();
                    if (obj == null || !"publish".equals(obj.toString())) {
                        this.hideFragment(frag);
                    }
                }
            }
        }

    }

    public void hideFragment(int fragmentId) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(fragmentId);
        if (fragment != null) {
            this.hideFragment(fragment);
        }

    }

    public void hideFragment(Fragment fragment) {
        if (fragment.isVisible()) {
            this.getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
        }

    }

    public boolean isAddedFragment(String name) {
        List<Fragment> fragmentList = this.getSupportFragmentManager().getFragments();
        if (fragmentList != null && !fragmentList.isEmpty()) {
            for(int i = fragmentList.size() - 1; i >= 0; --i) {
                if (fragmentList.get(i) != null && ((Fragment)fragmentList.get(i)).getClass() != null && ((Fragment)fragmentList.get(i)).getClass().getSimpleName().equals(name)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private void ensureUIHandler() {
        if (this.mUIHandler == null) {
            this.mUIHandler = new Handler(Looper.getMainLooper());
        }

    }

    protected void post(Runnable r) {
        this.ensureUIHandler();
        this.mUIHandler.post(r);
    }

    protected void postDelay(Runnable r, long delay) {
        this.ensureUIHandler();
        this.mUIHandler.postDelayed(r, delay);
    }

    protected void post(Runnable r, Object token) {
        this.ensureUIHandler();
        this.mUIHandler.postAtTime(r, token, SystemClock.uptimeMillis());
    }

    protected void post(Runnable r, Object token, long delay) {
        this.ensureUIHandler();
        this.mUIHandler.postAtTime(r, token, SystemClock.uptimeMillis() + delay);
    }

    protected void removeAllPost() {
        if (this.mUIHandler != null) {
            this.mUIHandler.removeCallbacksAndMessages((Object)null);
        }

    }

    protected void removePost(Object token) {
        this.ensureUIHandler();
        this.mUIHandler.removeCallbacksAndMessages(token);
    }

    protected void removePost(Runnable r) {
        this.ensureUIHandler();
        this.mUIHandler.removeCallbacks(r);
    }

    protected void releaseHandler() {
        if (this.mUIHandler != null) {
            this.mUIHandler.removeCallbacksAndMessages((Object)null);
        }

    }

    protected boolean isApplyEventBus() {
        return false;
    }

    protected void enableNetworkMonitor() {
        this.mEnableNetworkMonitor = true;
    }

    private void initNetworkMonitor() {
        if (this.mEnableNetworkMonitor) {
            this.mNetworkListener = new NetworkUtils.NetworkListener() {
                public void onNetworkStatusChanged(boolean b, NetworkUtils.NetInfo netInfo) {
                    try {
                        CompatActivity.this.onNetworkChanged(b, CompatActivity.this.mNetworkAvail != b, netInfo.type);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }

                    CompatActivity.this.mNetworkAvail = b;
                }
            };
            NetworkUtils.addNetworkListener(this.mNetworkListener);
        }

    }

    protected void onNetworkChanged(boolean networkAvail, boolean availChanged, int type) {
    }

    private void releaseNetworkMonitor() {
        if (this.mEnableNetworkMonitor && this.mNetworkListener != null) {
            NetworkUtils.removeNetworkListener(this.mNetworkListener);
        }

    }

    public void setDispatchView(View targetView) {
        targetView.setTag(R.id.component_touch_dispatch, this.getString(R.string.component_dispatch));
    }

    protected Activity getActivity() {
        return this;
    }

    public Activity getContext() {
        return this;
    }

    public <T> LifecycleTransformer<T> bindUntilDestroy() {
        return this.bindUntilEvent(ActivityEvent.DESTROY);
    }

    final class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                UiHandler.post(new Runnable() {
                    public void run() {
                        CompatActivity.this.processLocalBroadcastReceive(intent);
                    }
                });
            }

        }
    }
}
