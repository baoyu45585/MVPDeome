//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;

public class BarUtils {
    public BarUtils() {
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId != 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    @SuppressLint({"NewApi"})
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
        return !hasMenuKey && !hasBackKey;
    }

    public static void toggleNavigationBar(Activity activity, boolean isShow) {
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = (uiOptions | 4096) == uiOptions;
        if (!isImmersiveModeEnabled || isShow) {
            if (isImmersiveModeEnabled || !isShow) {
                if (VERSION.SDK_INT >= 14) {
                    newUiOptions = uiOptions ^ 2;
                }

                if (VERSION.SDK_INT >= 16) {
                    newUiOptions ^= 4;
                }

                if (VERSION.SDK_INT >= 18) {
                    newUiOptions ^= 4096;
                }

                activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            }
        }
    }

    public static BarUtils.NavigationBarInfo getNavigationBarInfo(Context context) {
        Point appUsableSize = ScreenUtils.getScreenSize(context);
        Point realScreenSize = ScreenUtils.getRealScreenSize(context);
        BarUtils.NavigationBarInfo navigationBarInfo = new BarUtils.NavigationBarInfo();
        if (appUsableSize.x < realScreenSize.x) {
            navigationBarInfo.mPoint = new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
            navigationBarInfo.orientation = 1;
            navigationBarInfo.isHasNavigationBar = true;
            return navigationBarInfo;
        } else if (appUsableSize.y < realScreenSize.y) {
            navigationBarInfo.mPoint = new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
            navigationBarInfo.orientation = 0;
            navigationBarInfo.isHasNavigationBar = true;
            return navigationBarInfo;
        } else {
            navigationBarInfo.mPoint = new Point();
            navigationBarInfo.orientation = 0;
            navigationBarInfo.isHasNavigationBar = false;
            return navigationBarInfo;
        }
    }

    public static class NavigationBarInfo {
        public Point mPoint;
        public int orientation;
        public boolean isHasNavigationBar;

        public NavigationBarInfo() {
        }
    }
}
