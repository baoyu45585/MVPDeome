//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {
    public EventBusUtils() {
    }

    public static void register(Object object) {
        EventBus.getDefault().register(object);
    }

    public static void unregister(Object object) {
        EventBus.getDefault().unregister(object);
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }
}
