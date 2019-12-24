//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.mvp;

import android.content.Context;

import com.hisunfly.one.component.uiframework.ViewState;


public interface MvpView {
    Context getContext();

    void setState(ViewState var1, Object... var2);

    void showState(ViewState var1, Object... var2);
}
