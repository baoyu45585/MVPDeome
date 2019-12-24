//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.rx;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface RxView {
    <T> LifecycleTransformer<T> bindUntilDestroy();

    <T> LifecycleTransformer<T> bindToLifecycle();
}
