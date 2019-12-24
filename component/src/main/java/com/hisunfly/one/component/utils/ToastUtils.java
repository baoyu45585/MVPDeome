//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.coder.zzq.smartshow.core.SmartShow;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.smartshow.toast.IPlainToastSetting.CustomViewCallback;
import com.hisunfly.one.component.R;
import com.hisunfly.one.component.app.AppModel;


public class ToastUtils {
    private static String TAG = "ToastUtils";
    private static boolean sIsShow;
    private static Context sApplicationContext;
    private static long slastShowTime;
    private static long INTERVAL_DURATION;
    private static CharSequence slastMessage;
    public static final int DEFAULT_TOAST_LAYOUT;

    public static void setIsShow(boolean isShow) {
        sIsShow = isShow;
    }

    public static void init(Application application) {
        SmartShow.init(application);
    }

    public static void initLayout(final int layoutResId) {
        SmartToast.plainSetting().customView(new CustomViewCallback() {
            public View createToastView(LayoutInflater layoutInflater) {
                View view = layoutInflater.inflate(layoutResId, (ViewGroup)null);
                return view;
            }
        }).backgroundColorRes(17170445);
    }

    public static void setApplicationContext(Application application) {
        sApplicationContext = application.getApplicationContext();
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(CharSequence message) {
        showImpl(message, 0);
    }

    public static void showShort(int message) {
        showImpl(getString(message), 0);
    }

    public static void showLong(CharSequence message) {
        showImpl(message, 1);
    }

    public static void showLong(int message) {
        showImpl(getString(message), 1);
    }

    public static void show(CharSequence message, int duration) {
        showImpl(message, duration);
    }

    public static void show(int message, int duration) {
        showImpl(getString(message), duration);
    }

    private static String getString(int resId) {
        return getApplicationContext() != null ? getApplicationContext().getString(resId) : "";
    }

    private static void showImpl(final CharSequence message, final int duration) {
        if (sIsShow && message != null) {
            long delay = 0L;
            long currentTimeMillis = System.currentTimeMillis();
            if (slastShowTime == 0L) {
                slastShowTime = currentTimeMillis;
                slastMessage = message;
                PrintLog.e(TAG, "slastShowTime = " + slastShowTime);
            } else {
                long diffTime = currentTimeMillis - slastShowTime;
                PrintLog.e(TAG, "diffTime = " + diffTime);
                if (diffTime >= 0L) {
                    if (diffTime > INTERVAL_DURATION) {
                        slastShowTime = currentTimeMillis;
                        PrintLog.e(TAG, "间隔大于" + INTERVAL_DURATION + "毫秒");
                    } else {
                        PrintLog.e(TAG, "当前时间大于上次显示的时间，但是小于" + INTERVAL_DURATION + "毫秒");
                        if (slastMessage != null && slastMessage.toString().equals(message.toString())) {
                            PrintLog.e(TAG, INTERVAL_DURATION + "毫秒内显示相同的数据=" + message);
                            return;
                        }

                        slastShowTime += INTERVAL_DURATION;
                        delay = INTERVAL_DURATION;
                    }
                } else {
                    slastShowTime += INTERVAL_DURATION;
                    delay = slastShowTime - currentTimeMillis;
                    PrintLog.e(TAG, "当前时间小于上次显示的时间 = delay=" + delay);
                }
            }

            slastMessage = message;
            PrintLog.e(TAG, "最终delay = " + delay);
            UiHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        ToastUtils.showImpl0(message, duration);
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }

                }
            }, delay);
        }

    }

    private static void showImpl0(CharSequence message, int duration) {
        PrintLog.e("ToastUtils", "showImpl0()=" + message);
        if (duration == 1) {
            SmartToast.showLongInCenter(message);
        } else {
            SmartToast.showInCenter(message);
        }

    }

    static {
        setApplicationContext(AppModel.getApplication());
        sIsShow = true;
        slastShowTime = 0L;
        INTERVAL_DURATION = 1000L;
        slastMessage = null;
        DEFAULT_TOAST_LAYOUT = R.layout.custom_smart_toast;
    }
}
