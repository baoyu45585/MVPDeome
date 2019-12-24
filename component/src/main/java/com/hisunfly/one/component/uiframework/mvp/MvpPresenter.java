//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.mvp;

import android.os.Bundle;

public interface MvpPresenter {
    void onCreate(Bundle var1);

    void onPause();

    void stop();

    void resume();

    void onDestroy();

    void getData(Object... var1);

    void onShow();

    void onHide();
}
