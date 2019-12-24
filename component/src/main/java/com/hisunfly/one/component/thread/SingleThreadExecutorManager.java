//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorManager {
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public SingleThreadExecutorManager() {
    }

    public static void execute(Runnable r) {
        singleThreadExecutor.execute(r);
    }
}
