//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class EqWeakReference<T> extends WeakReference<T> {
    public EqWeakReference(T referent) {
        super(referent);
    }

    public boolean equals(Object obj) {
        return obj instanceof Reference ? this.get().equals(((Reference)obj).get()) : this.get().equals(obj);
    }

    public int hashCode() {
        return this.get().hashCode();
    }
}
