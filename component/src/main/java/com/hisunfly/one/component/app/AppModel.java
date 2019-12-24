//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.IntRange;
import android.text.TextUtils;

import com.hisunfly.one.component.utils.EqWeakReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AppModel {
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list";
    private static Application sApplication;
    private static final List<EqWeakReference<Activity>> sManagedActivities = new LinkedList();
    private static HashMap<Class, ActivityInstanceState> sActivityInstanceStates = new HashMap();
    private static EqWeakReference<Activity> mCurrentActivity;
    private static String sAppVersionName;
    private static String sAppVersionCode;
    private static String sChannelCode;

    public AppModel() {
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getApplicationContext() {
        return sApplication.getApplicationContext();
    }

    public static String getAppVersionName() {
        if (sAppVersionName != null) {
            return sAppVersionName;
        } else {
            try {
                sAppVersionName = sApplication.getPackageManager().getPackageInfo(sApplication.getPackageName(), 0).versionName;
            } catch (NameNotFoundException var1) {
                return "获取版本号失败";
            }

            return sAppVersionName;
        }
    }

    public static String getAppVersionCode() {
        if (sAppVersionCode != null) {
            return sAppVersionCode;
        } else {
            try {
                sAppVersionCode = String.valueOf(sApplication.getPackageManager().getPackageInfo(sApplication.getPackageName(), 0).versionCode);
            } catch (NameNotFoundException var1) {
                var1.printStackTrace();
                return String.valueOf(2147483647);
            }

            return sAppVersionCode;
        }
    }

    public static String getAppChannel() {
        if (!TextUtils.isEmpty(sChannelCode)) {
            return sChannelCode;
        } else {
            try {
                Object channel = sApplication.getPackageManager().getApplicationInfo(sApplication.getPackageName(), 128).metaData.get("STAR_CHANNEL");
                sChannelCode = channel.toString().trim();
                return sChannelCode;
            } catch (Exception var1) {
                var1.printStackTrace();
                return "10000";
            }
        }
    }

    public static void setActivityLimit(Class activityCls, int limit) {
        if (!sActivityInstanceStates.containsKey(activityCls)) {
            AppModel.ActivityInstanceState state = new AppModel.ActivityInstanceState();
            state.limit = limit;
            state.size = 0;
            sActivityInstanceStates.put(activityCls, state);
        }

    }

    public static void registerActivity(Activity activity) {
        if (activity != null) {
            EqWeakReference<Activity> activityRef = new EqWeakReference(activity);
            AppModel.ActivityInstanceState state = (AppModel.ActivityInstanceState)sActivityInstanceStates.get(activity.getClass());
            if (state != null) {
                if (state.limit > 0 && state.limit == state.size) {
                    EqWeakReference<Activity> removed = (EqWeakReference)state.activities.remove(0);
                    if (removed.get() != null) {
                        ((Activity)removed.get()).finish();
                    } else {
                        --state.size;
                        sManagedActivities.remove(removed);
                    }
                }

                state.activities.add(activityRef);
                ++state.size;
            }

            sManagedActivities.add(activityRef);
        }
    }

    public static void removeActivity(Activity activity) {
        if (activity != null) {
            EqWeakReference<Activity> activityRef = new EqWeakReference(activity);
            sManagedActivities.remove(activityRef);
            AppModel.ActivityInstanceState activityInstanceState = (AppModel.ActivityInstanceState)sActivityInstanceStates.get(activity.getClass());
            if (activityInstanceState != null) {
                activityInstanceState.activities.remove(activityRef);
                --activityInstanceState.size;
            }

        }
    }

    public static void finishActivities() {
        Iterator var0 = sManagedActivities.iterator();

        while(var0.hasNext()) {
            WeakReference<Activity> aRef = (WeakReference)var0.next();
            Activity a = (Activity)aRef.get();
            if (null != a && !a.isFinishing()) {
                a.finish();
            }
        }

    }

    public static Activity getTopActivity() {
        return sManagedActivities.size() > 0 ? (Activity)((EqWeakReference)sManagedActivities.get(sManagedActivities.size() - 1)).get() : null;
    }

    public static Activity getActivity(@IntRange(from = 0L) int index) {
        if (sManagedActivities.size() <= 0) {
            return null;
        } else if (sManagedActivities.size() - 1 < index) {
            return null;
        } else {
            try {
                return (Activity)((EqWeakReference)sManagedActivities.get(sManagedActivities.size() - 1 - index)).get();
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static void setCurrentActivity(Activity currentActivity) {
        if (currentActivity != null) {
            EqWeakReference<Activity> activityRef = new EqWeakReference(currentActivity);
            mCurrentActivity = activityRef;
        } else {
            mCurrentActivity = null;
        }

    }

    public static Activity getCurrentActivity() {
        if (mCurrentActivity != null) {
            return mCurrentActivity.get() != null ? (Activity)mCurrentActivity.get() : getTopActivity();
        } else {
            return getTopActivity();
        }
    }

    public static void exit() {
        finishActivities();
    }

    public static boolean getBoolMetaData(String metaName) {
        try {
            ApplicationInfo appInfo = getApplication().getPackageManager().getApplicationInfo(getApplication().getPackageName(), 128);
            if (appInfo != null) {
                boolean val = appInfo.metaData.getBoolean(metaName, false);
                return val;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return false;
    }

    public static String getMetaData(String metaName) {
        ApplicationInfo appInfo = null;

        try {
            appInfo = getApplication().getPackageManager().getApplicationInfo(getApplication().getPackageName(), 128);
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        if (appInfo != null) {
            try {
                String val = appInfo.metaData.getString(metaName);
                return val;
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return null;
    }

    public static int getMetaDataInt(String metaName, int defaultValue) {
        ApplicationInfo appInfo = null;

        try {
            appInfo = getApplication().getPackageManager().getApplicationInfo(getApplication().getPackageName(), 128);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (appInfo != null) {
            try {
                int val = appInfo.metaData.getInt(metaName, defaultValue);
                return val;
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return -1;
    }

    private static class ActivityInstanceState {
        int limit;
        int size;
        final List<EqWeakReference<Activity>> activities;

        private ActivityInstanceState() {
            this.activities = new ArrayList();
        }
    }
}
