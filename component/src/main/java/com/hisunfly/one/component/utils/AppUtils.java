//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Looper;
import android.os.Process;
import android.os.Build.VERSION;
import android.support.v4.content.FileProvider;

import com.hisunfly.one.component.app.AppModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean checkContext(Context context) {
        if (context == null) {
            return false;
        } else if (context instanceof Activity) {
            Activity activity = (Activity)context;
            return isActivityRunning(activity);
        } else {
            return true;
        }
    }

    public static boolean isActivityRunning(Activity activity) {
        try {
            return activity != null && !activity.isFinishing() && (VERSION.SDK_INT < 17 || !activity.isDestroyed());
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isMainProcess(Context context) {
        String processName = getProcessName(context);
        return processName != null && !processName.contains(":");
    }

    public static boolean isInProcess(Context context, String pname) {
        String processName = getProcessName(context);
        return processName != null && processName.equals(pname);
    }

    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        } else {
            Iterator var4 = runningApps.iterator();

            RunningAppProcessInfo procInfo;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                procInfo = (RunningAppProcessInfo)var4.next();
            } while(procInfo.pid != pid);

            return procInfo.processName;
        }
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        } else {
            Iterator var4 = runningApps.iterator();

            RunningAppProcessInfo procInfo;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                procInfo = (RunningAppProcessInfo)var4.next();
            } while(procInfo.pid != pid);

            return procInfo.processName;
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return 0;
        }
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager myAM = (ActivityManager)context.getSystemService("activity");
        ArrayList<RunningServiceInfo> runningServices = (ArrayList)myAM.getRunningServices(2147483647);

        for(int i = 0; i < runningServices.size(); ++i) {
            if (((RunningServiceInfo)runningServices.get(i)).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isAppRunningBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = ((RunningTaskInfo)tasks.get(0)).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public static String getPackageName(Context context) {
        String packageName = "";
        PackageInfo pi = getCurrentPackageInfo(context);
        if (null != pi) {
            packageName = pi.packageName;
        }

        return packageName;
    }

    public static boolean isPkgInstalled(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (NameNotFoundException var3) {
            return false;
        }
    }

    public static PackageInfo getCurrentPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi;
        } catch (Exception var3) {
            return null;
        }
    }

    public static void openAppByPkgName(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            PackageManager pm = context.getPackageManager();
            Intent resolveIntent = new Intent("android.intent.action.MAIN", (Uri)null);
            resolveIntent.addCategory("android.intent.category.LAUNCHER");
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = (ResolveInfo)apps.iterator().next();
            if (ri != null) {
                String pkgName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                ComponentName cn = new ComponentName(pkgName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (NameNotFoundException var11) {
            var11.printStackTrace();
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    public static void exit(Context context) {
        try {
            System.exit(0);
            Process.killProcess(Process.myPid());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static String getPkgVersionName(String packageName) {
        String versionName = "";

        try {
            PackageManager pm = AppModel.getApplication().getPackageManager();
            versionName = pm.getPackageInfo(packageName, 0).versionName;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return versionName;
    }

    public static void openUrl(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        boolean started = ContextUtils.startActivity(context, intent);
        if (!started) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.addCategory("android.intent.category.BROWSABLE");
            ContextUtils.startActivity(context, intent);
        }

    }

    public static String getCurProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService("activity");
        Iterator var3 = mActivityManager.getRunningAppProcesses().iterator();

        RunningAppProcessInfo appProcess;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            appProcess = (RunningAppProcessInfo)var3.next();
        } while(appProcess.pid != pid);

        return appProcess.processName;
    }

    public static boolean isWeChatAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for(int i = 0; i < pinfo.size(); ++i) {
                String pn = ((PackageInfo)pinfo.get(i)).packageName;
                if ("com.tencent.mm".equals(pn)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void installApk(Context context, String apkPath) {
        try {
            Intent intent = getInstallApkIntent(apkPath);
            context.startActivity(intent);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static Intent getInstallApkIntent(String apkPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        Uri uri = null;
        if (VERSION.SDK_INT >= 24) {
            intent.addFlags(3);
            uri = FileProvider.getUriForFile(AppModel.getApplicationContext(), AppModel.getApplication().getPackageName() + ".fileProvider", new File(apkPath));
        } else {
            uri = Uri.fromFile(new File(apkPath));
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }
}
