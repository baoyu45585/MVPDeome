//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class PrintLog {
    private static boolean sPrintLog = false;
    private static final HashMap<String, Long> sTimeStamps = new HashMap();
    public static final String TAG = "miguqq";
    private static final int MAX_LOG_LENGTH = 3072;

    public PrintLog() {
    }

    public static void setPrintLogEnabled(boolean enable) {
        sPrintLog = enable;
    }

    public static boolean isPrintLog() {
        return sPrintLog;
    }

    public static void i(String msg) {
        i("miguqq", msg);
    }

    public static void i(String tag, String msg) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.i(tag, "error: msg is NULL");
                return;
            }

            long length = (long)msg.length();
            if (length <= 3072L) {
                Log.i(tag, msg);
            } else {
                while(msg.length() > 3072) {
                    String logContent = msg.substring(0, 3072);
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }

                Log.i(tag, msg + "\n\n");
            }
        }

    }

    public static void v(String msg) {
        v("miguqq", msg);
    }

    public static void v(String tag, String msg) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.v(tag, "error: msg is NULL");
            } else {
                Log.v(tag, msg);
            }
        }

    }

    public static void d(String msg) {
        d("miguqq", msg);
    }

    public static void d(String tag, String msg) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.d(tag, "error: msg is NULL");
            } else {
                Log.d(tag, msg);
            }
        }

    }

    public static void w(String tag, String msg, Exception ex) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.w(tag, "error: msg is NULL");
            } else {
                Log.w(tag, msg, ex);
            }
        }

    }

    public static void w(String msg) {
        w("miguqq", msg);
    }

    public static void w(String tag, String msg) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.w(tag, "error: msg is NULL");
            } else {
                Log.w(tag, msg);
            }
        }

    }

    public static void e(String msg) {
        e("miguqq", msg);
    }

    public static void e(String tag, String msg) {
        if (sPrintLog) {
            if (TextUtils.isEmpty(msg)) {
                Log.e(tag, "error: msg is NULL");
                return;
            }

            long length = (long)msg.length();
            if (length <= 3072L) {
                Log.e(tag, msg);
            } else {
                while(msg.length() > 3072) {
                    String logContent = msg.substring(0, 3072);
                    msg = msg.replace(logContent, "");
                    Log.e(tag, logContent);
                }

                Log.e(tag, msg + "\n\n");
            }
        }

    }

    public static void e(Throwable t) {
        if (sPrintLog && t != null) {
            t.printStackTrace();
        }

    }

    public static long startTimeTrack(String tag) {
        if (sPrintLog) {
            long time = System.currentTimeMillis();
            sTimeStamps.put(tag, time);
            return time;
        } else {
            return -1L;
        }
    }

    public static long passTimeTrack(String tag) {
        if (sPrintLog) {
            Long startTime = (Long)sTimeStamps.get(tag);
            if (startTime != null) {
                long timeCost = System.currentTimeMillis() - startTime;
                d("TimeCost__" + tag + "__", timeCost + "");
                sTimeStamps.put(tag, System.currentTimeMillis());
                return timeCost;
            }
        }

        return -1L;
    }

    public static long passTimeTrack(String tag, String subTag) {
        if (sPrintLog) {
            Long startTime = (Long)sTimeStamps.get(tag);
            if (startTime != null) {
                long timeCost = System.currentTimeMillis() - startTime;
                d("TimeCost__" + tag + "__" + subTag, timeCost + "");
                sTimeStamps.put(tag, System.currentTimeMillis());
                return timeCost;
            }
        }

        return -1L;
    }

    public static long endTimeTrack(String tag) {
        if (sPrintLog) {
            Long startTime = (Long)sTimeStamps.get(tag);
            if (startTime != null) {
                long timeCost = System.currentTimeMillis() - startTime;
                d("TimeCost__" + tag, timeCost + "");
                sTimeStamps.remove(tag);
                return timeCost;
            }
        }

        return -1L;
    }

    public static long endTimeTrack(String tag, String sub) {
        if (sPrintLog) {
            Long startTime = (Long)sTimeStamps.get(tag);
            if (startTime != null) {
                long timeCost = System.currentTimeMillis() - startTime;
                d("TimeCost__" + sub + "__" + tag, timeCost + "");
                sTimeStamps.remove(tag);
                return timeCost;
            }
        }

        return -1L;
    }
}
