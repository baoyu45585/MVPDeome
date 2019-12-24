//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrashHandlers implements UncaughtExceptionHandler {
    private final List<UncaughtExceptionHandler> mHandlers;
    private final UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandlers getInstance() {
        return CrashHandlers.InstanceHolder.instance;
    }

    private CrashHandlers() {
        this.mHandlers = new ArrayList();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public boolean addHandler(UncaughtExceptionHandler handler) {
        return this.mHandlers.add(handler);
    }

    public boolean removeHandler(UncaughtExceptionHandler handler) {
        return this.mHandlers.remove(handler);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        Iterator var3 = this.mHandlers.iterator();

        while(var3.hasNext()) {
            UncaughtExceptionHandler handler = (UncaughtExceptionHandler)var3.next();
            handler.uncaughtException(thread, ex);
        }

        if (this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    private static class InstanceHolder {
        private static final CrashHandlers instance = new CrashHandlers();

        private InstanceHolder() {
        }
    }
}
