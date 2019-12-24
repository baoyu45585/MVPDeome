//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.lang.reflect.InvocationTargetException;

public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();
            getDisplay(context).getMetrics(outMetrics);
            return outMetrics.widthPixels;
        }
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();
            getDisplay(context).getMetrics(outMetrics);
            return outMetrics.heightPixels;
        }
    }

    public static Display getDisplay(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService("window");
        return manager.getDefaultDisplay();
    }

    public static void setVerticalScreen(Activity activity) {
        activity.setRequestedOrientation(1);
    }

    public static void setHorizontalScreen(Activity activity) {
        activity.setRequestedOrientation(0);
    }

    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        if (VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer)Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer)Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException var5) {
                ;
            } catch (InvocationTargetException var6) {
                ;
            } catch (NoSuchMethodException var7) {
                ;
            }
        }

        return size;
    }

    public static int getScreenBrightness(Activity activity) {
        int screenBrightness = 50;

        try {
            screenBrightness = System.getInt(activity.getContentResolver(), "screen_brightness");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return screenBrightness;
    }

    public static void setScreenBrightness(Activity activity, int brightnessValue) {
        Window localWindow = activity.getWindow();
        LayoutParams localLayoutParams = localWindow.getAttributes();
        float f;
        if (brightnessValue > 15 && brightnessValue <= 255) {
            f = (float)brightnessValue / 255.0F;
            localLayoutParams.screenBrightness = f;
            localWindow.setAttributes(localLayoutParams);
        } else if (brightnessValue >= 0 && brightnessValue <= 15) {
            f = 0.05882353F;
            localLayoutParams.screenBrightness = f;
            localWindow.setAttributes(localLayoutParams);
        }

    }

    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
