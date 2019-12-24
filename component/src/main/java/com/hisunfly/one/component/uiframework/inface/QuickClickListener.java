//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.inface;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class QuickClickListener implements OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 800;
    long lastClickTime = 0L;

    public QuickClickListener() {
    }

    protected abstract void onQuickClick(View var1);

    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastClickTime > 800L) {
            this.lastClickTime = currentTime;
            this.onQuickClick(v);
        }

    }
}
