//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtil {
    public SoftKeyboardUtil() {
    }

    public static boolean isKeybordShow(Context mContext) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService("input_method");
        return imm.isActive();
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService("input_method");
        imm.showSoftInput(view, 2);
        imm.toggleSoftInput(2, 1);
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService("input_method");
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }

    }
}
