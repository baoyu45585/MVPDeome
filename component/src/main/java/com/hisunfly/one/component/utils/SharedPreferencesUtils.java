//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;

import com.hisunfly.one.component.app.AppModel;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SharedPreferencesUtils {
    private static int sharemode = 3;

    public SharedPreferencesUtils() {
    }

    public static int getSharemode() {
        int _sdkLevel = VERSION.SDK_INT;
        sharemode = _sdkLevel > 8 ? 4 : sharemode;
        return sharemode;
    }

    public static long ReadSharedPreferencesLong(String name, String key) {
        try {
            SharedPreferences userInfo =  AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getLong(key, -1L);
        } catch (NullPointerException var3) {
            return -1L;
        }
    }

    public static long ReadSharedPreferencesLong(String name, String key, long defaulValue) {
        try {
            SharedPreferences userInfo =  AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getLong(key, defaulValue);
        } catch (NullPointerException var5) {
            return defaulValue;
        }
    }

    public static boolean ReadSharedPreferencesBoolean(String name, String key) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getBoolean(key, false);
        } catch (NullPointerException var3) {
            return true;
        }
    }

    public static boolean ReadSharedPreferencesBooleanTrue(String name, String key) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getBoolean(key, true);
        } catch (NullPointerException var3) {
            return true;
        }
    }

    public static boolean ReadSharedPreferencesBoolean(String name, String key, boolean defaultValue) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getBoolean(key, defaultValue);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static String ReadSharedPreferencesString(String name, String key) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getString(key, (String)null);
        } catch (Exception var3) {
            return null;
        }
    }

    public static String ReadSharedPreferencesString(String name, String key, String defaultvalue) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getString(key, defaultvalue);
        } catch (Exception var4) {
            return null;
        }
    }

    public static void WriteSharedPreferences(String name, String key, String value) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        userInfoEditor.putString(key, value);
        userInfoEditor.apply();
    }

    public static void WriteSharedPreferences(String name, String key, boolean value) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        userInfoEditor.putBoolean(key, value);
        userInfoEditor.apply();
    }

    public static void WriteSharedPreferencesLong(String name, String key, long value) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        userInfoEditor.putLong(key, value);
        userInfoEditor.apply();
    }

    public static void WriteSharedPreferencesInt(String name, String key, int value) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        userInfoEditor.putInt(key, value);
        userInfoEditor.apply();
    }

    public static void WriteSharedPreferencesStringHashMap(String name, HashMap map) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        Iterator iter = map.entrySet().iterator();

        while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            userInfoEditor.putString((String)key, (String)val);
        }

        userInfoEditor.apply();
    }

    public static int ReadSharedPreferencesInt(String name, String key, int defValue) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getInt(key, defValue);
        } catch (NullPointerException var4) {
            return defValue;
        }
    }

    public static void WriteSharedPreferencesFloat(String name, String key, float value) {
        Editor userInfoEditor = AppModel.getApplication().getSharedPreferences(name, getSharemode()).edit();
        userInfoEditor.putFloat(key, value);
        userInfoEditor.commit();
    }

    public static float ReadSharedPreferencesFloat(String name, String key, float defaulValue) {
        try {
            SharedPreferences userInfo = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            return userInfo.getFloat(key, defaulValue);
        } catch (NullPointerException var4) {
            return defaulValue;
        }
    }

    public static void registerListener(String name, OnSharedPreferenceChangeListener listener) {
        try {
            SharedPreferences sp = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            sp.registerOnSharedPreferenceChangeListener(listener);
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        }

    }

    public static void unregisterListener(String name, OnSharedPreferenceChangeListener listener) {
        try {
            SharedPreferences sp =AppModel.getApplication().getSharedPreferences(name, getSharemode());
            sp.unregisterOnSharedPreferenceChangeListener(listener);
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        }

    }

    public static Object readField(String file, String key) {
        SharedPreferences sp = AppModel.getApplication().getSharedPreferences(file, getSharemode());
        return sp.getAll().get(key);
    }

    public static void writeField(String file, String key, Object value) {
        SharedPreferences sp = AppModel.getApplication().getSharedPreferences(file, getSharemode());
        if (value instanceof String) {
            sp.edit().putString(key, (String)value).commit();
        } else if (value.getClass() == Integer.class) {
            sp.edit().putInt(key, (Integer)value).commit();
        } else if (value.getClass() == Long.class) {
            sp.edit().putLong(key, (Long)value).commit();
        } else if (value.getClass() == Float.class) {
            sp.edit().putFloat(key, (Float)value).commit();
        } else if (value.getClass() == Boolean.class) {
            sp.edit().putBoolean(key, (Boolean)value).commit();
        }

    }

    public static <T> T readEntity(String name, Class<T> clss) {
        try {
            T ins = clss.newInstance();
            Field[] fields = clss.getDeclaredFields();
            if (fields != null) {
                Field[] var4 = fields;
                int var5 = fields.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field field = var4[var6];
                    field.setAccessible(true);
                    String keyName = null;
                    SharedPreferencesField annotation = (SharedPreferencesField)field.getAnnotation(SharedPreferencesField.class);
                    if (annotation != null) {
                        keyName = annotation.value();
                    } else {
                        keyName = field.getName();
                    }

                    Object object = readField(name, keyName);
                    if (object != null) {
                        field.set(ins, object);
                    }
                }
            }

            return ins;
        } catch (InstantiationException var11) {
            var11.printStackTrace();
        } catch (IllegalAccessException var12) {
            var12.printStackTrace();
        }

        return null;
    }

    public static <T> void writeEntity(String name, T object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            Field[] var3 = fields;
            int var4 = fields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                field.setAccessible(true);
                Object value = field.get(object);
                SharedPreferencesField spf = (SharedPreferencesField)field.getAnnotation(SharedPreferencesField.class);
                field.getAnnotations();
                if (spf != null && value != null) {
                    writeField(name, spf.value(), value);
                }
            }
        } catch (IllegalArgumentException var9) {
            var9.printStackTrace();
        } catch (IllegalAccessException var10) {
            var10.printStackTrace();
        }

    }

    public static void clearSharedPerferences(String name) {
        try {
            SharedPreferences sp = AppModel.getApplication().getSharedPreferences(name, getSharemode());
            sp.edit().clear().commit();
        } catch (NullPointerException var2) {
            var2.printStackTrace();
        }

    }
}
