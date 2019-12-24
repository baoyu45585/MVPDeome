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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import com.hisunfly.one.component.uiframework.fragment.LazyFragment;
import com.hisunfly.one.component.uiframework.mvp.RxMvpView;
import com.hisunfly.one.component.utils.BarUtils;
import com.hisunfly.one.component.utils.EventBusUtils;
import com.hisunfly.one.component.utils.NetworkUtils;
import com.hisunfly.one.component.utils.PrintLog;
import com.hisunfly.one.component.utils.UiHandler;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CompatLazyFragment extends LazyFragment implements RxMvpView {
    private static final int SHOW_HIDE_DELAY_TIME = 60;
    protected Unbinder mUnbinder;
    private boolean mEnableNetworkMonitor;
    private NetworkUtils.NetworkListener mNetworkListener;
    private boolean mNetworkAvail;
    protected Handler mUIHandler;
    private boolean mHandleKeyboardVisible;
    private boolean mOnCreate;
    boolean mIsHandOnShow = false;
    boolean mIsHandOnHide = false;
    private Activity mActivity;
    protected FragmentManager mActivityFragmentManager;
    private CompatLazyFragment.MyBroadcastReceiver mMyBroadcastReceiver;

    public CompatLazyFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mOnCreate = true;
        this.mActivity = this.getActivity();
        this.mActivityFragmentManager = this.getActivity().getSupportFragmentManager();
        Bundle arguments = this.getArguments();

        try {
            this.onBeforeinit0(savedInstanceState);
            this.onBeforeInit(arguments);
        } catch (Exception var4) {
            var4.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var4;
            }
        }

    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {
        this.init_(savedInstanceState);
        super.onCreateViewLazy(savedInstanceState);

        try {
            View contentView = this.setContainerView();
            if (this.isEnableButterKnife()) {
                this.mUnbinder = ButterKnife.bind(this, contentView);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var3;
            }
        }

    }

    protected final void onFragmentStartLazy0() {
        PrintLog.e(this.TAG, "onFragmentStartLazy0() 0");
        if (this.mOnCreate) {
            PrintLog.e(this.TAG, "onFragmentStartLazy0() 1");
            this.mOnCreate = false;

            try {
                this.initLayout();
                this.setListener();
                this.initData();
                this.initNetworkMonitor();
                if (this.isApplyEventBus()) {
                    EventBusUtils.register(this);
                }
            } catch (Exception var2) {
                var2.printStackTrace();
                if (PrintLog.isPrintLog()) {
                    throw var2;
                }
            }
        }

    }

    private void init_(Bundle savedInstanceState) {
        try {
            this.afterInject(savedInstanceState);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void afterInject(Bundle savedInstanceState) {
        this.initComponent(savedInstanceState);
    }

    protected void onBeforeinit0(Bundle savedInstanceState) {
    }

    protected void onBeforeInit(Bundle arguments) {
    }

    protected abstract void initComponent(Bundle var1);

    protected abstract int getLayout();

    protected abstract View setContainerView();

    protected abstract void initLayout();

    protected void setListener() {
    }

    protected abstract void initData();

    protected boolean isEnableButterKnife() {
        return true;
    }

    protected void enableKeyboardVisibilityHandle() {
        this.mHandleKeyboardVisible = true;
    }

    public boolean isHandleKeyboardVisible() {
        return this.mHandleKeyboardVisible;
    }

    public void onKeyboardVisibilityChanged(boolean visibility) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    protected boolean isImmersionBarEnable() {
        return this.getCompatActivity().isFinalImmersionBarEnable();
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
            if (this.isImmersionBarEnable()) {
                MarginLayoutParams mLp = (MarginLayoutParams)targetView.getLayoutParams();
                mLp.topMargin = BarUtils.getStatusBarHeight(this.getContext());
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void ensureLocalBroadcast(IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mMyBroadcastReceiver = new CompatLazyFragment.MyBroadcastReceiver(), intentFilter);
    }

    private void unregisterLocalBroadcastReceiver() {
        if (this.mMyBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mMyBroadcastReceiver);
        }

    }

    protected void processLocalBroadcastReceive(Intent intent) {
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        PrintLog.e(this.TAG, "setUserVisibleHint getUserVisibleHint()=" + this.getUserVisibleHint() + ";isVisibleToUser=" + isVisibleToUser + ";isInit=" + this.isInit + ";isResumed=" + this.isResumed());
        if (isVisibleToUser != this.getUserVisibleHint()) {
            if (!isVisibleToUser) {
                if (this.isInit) {
                    this.onHideImpl();
                }
            } else {
                this.onShowImpl();
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onResumeLazy() {
        super.onResumeLazy();
        PrintLog.e(this.TAG, "onResumeLazy 0");
        if (!this.isHidden() && this.getUserVisibleHint()) {
            PrintLog.e(this.TAG, "onResumeLazy 1");
            this.onShowImpl();
        }

    }

    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        PrintLog.e(this.TAG, "onFragmentStopLazy");
        this.onHideImpl();
    }

    protected void onDestroyViewLazy() {
        try {
            if (this.mUnbinder != null) {
                this.mUnbinder.unbind();
            }

            this.releaseNetworkMonitor();
            this.removeAllPost();
        } catch (Exception var5) {
            var5.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var5;
            }
        } finally {
            super.onDestroyViewLazy();
        }

    }

    public void onDestroy() {
        try {
            if (this.isApplyEventBus()) {
                EventBusUtils.unregister(this);
            }

            this.unregisterLocalBroadcastReceiver();
        } catch (Exception var5) {
            var5.printStackTrace();
            if (PrintLog.isPrintLog()) {
                throw var5;
            }
        } finally {
            super.onDestroy();
        }

    }

    private void onShowImpl() {
        if (!this.mIsHandOnShow) {
            this.mIsHandOnShow = true;
            UiHandler.postDelayed(new Runnable() {
                public void run() {
                    CompatLazyFragment.this.mIsHandOnShow = false;
                }
            }, 60L);
            this.onShow();
        }
    }

    private void onHideImpl() {
        if (!this.mIsHandOnHide) {
            this.mIsHandOnHide = true;
            UiHandler.postDelayed(new Runnable() {
                public void run() {
                    CompatLazyFragment.this.mIsHandOnHide = false;
                }
            }, 60L);
            this.onHide();
        }
    }

    protected void onShow() {
        PrintLog.e(this.TAG, "onShow()");
    }

    protected void onHide() {
        PrintLog.e(this.TAG, "onHide()");
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
                        CompatLazyFragment.this.onNetworkChanged(b, CompatLazyFragment.this.mNetworkAvail != b, netInfo.type);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }

                    CompatLazyFragment.this.mNetworkAvail = b;
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

    protected boolean onBackPressed() {
        boolean fragmentHandled = false;
        if (!this.isAdded()) {
            return fragmentHandled;
        } else {
            List<Fragment> fragments = this.getChildFragmentManager().getFragments();
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
                    }

                    if (fragment instanceof CompatFragment) {
                        CompatFragment bf = (CompatFragment)fragment;
                        if (bf.onBackPressed()) {
                            fragmentHandled = true;
                            break;
                        }
                    }
                }
            }

            return fragmentHandled;
        }
    }

    protected void finishActivity() {
        try {
            ActivityCompat.finishAfterTransition(this.mActivity);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    @RequiresApi(
        api = 17
    )
    public void show(FragmentActivity activity) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.getSupportFragmentManager().beginTransaction().add(16908290, this, this.toString()).commitAllowingStateLoss();
        }
    }

    public void showSelf() {
        try {
            this.mActivityFragmentManager.beginTransaction().show(this).commitAllowingStateLoss();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void hideSelf() {
        try {
            this.mActivityFragmentManager.beginTransaction().hide(this).commitAllowingStateLoss();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void showSelf(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit) {
        try {
            FragmentTransaction fragmentTransaction = this.mActivityFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(enter, exit);
            fragmentTransaction.show(this).commitAllowingStateLoss();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void hideSelf(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit) {
        try {
            FragmentTransaction fragmentTransaction = this.mActivityFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(enter, exit);
            fragmentTransaction.hide(this).commitAllowingStateLoss();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public CompatActivity getCompatActivity() {
        return (CompatActivity)this.getActivity();
    }

    public void removeFragment(Class fragmentClss) {
        Fragment fragment = this.getChildFragmentManager().findFragmentByTag(fragmentClss.getName());
        if (fragment != null) {
            this.getChildFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }

    }

    public void hideFragment(int fragmentId) {
        Fragment fragment = this.getChildFragmentManager().findFragmentById(fragmentId);
        if (fragment != null) {
            this.hideFragment(fragment);
        }

    }

    public void hideFragment(Fragment fragment) {
        if (fragment.isVisible()) {
            this.getChildFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
        }

    }

    public void showFragment(int container, Fragment fragment) {
        this.showFragment(container, fragment, false);
    }

    public void showFragment(int container, Fragment fragment, boolean hideOther) {
        if (fragment.isAdded()) {
            this.getChildFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        } else {
            this.getChildFragmentManager().beginTransaction().add(container, fragment, fragment.getClass().getName()).commitAllowingStateLoss();
        }

        if (hideOther) {
            List<Fragment> fragmentList = this.getChildFragmentManager().getFragments();
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

    public boolean isAddedFragment(String name) {
        List<Fragment> fragmentList = this.getChildFragmentManager().getFragments();
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

    public Context getContext() {
        return this.mActivity;
    }

    public <T> LifecycleTransformer<T> bindUntilDestroy() {
        return this.bindUntilEvent(FragmentEvent.DESTROY);
    }

    final class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                UiHandler.post(new Runnable() {
                    public void run() {
                        CompatLazyFragment.this.processLocalBroadcastReceive(intent);
                    }
                });
            }

        }
    }
}
