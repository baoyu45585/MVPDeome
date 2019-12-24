//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.hisunfly.one.component.app.AppModel;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NetworkUtils {
    public static final int NETWORK_TYPE_NONE = -1;
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_2G = 2;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_4G = 4;
    public static final String NETWORK_TYPESTRING_UNKNOWN = "?";
    public static final String NETWORK_TYPESTRING_NONE = "-";
    public static final String NETWORK_TYPESTRING_WIFI = "WiFi";
    public static final String NETWORK_TYPESTRING_2G = "2G";
    public static final String NETWORK_TYPESTRING_3G = "3G";
    public static final String NETWORK_TYPESTRING_4G = "4G";
    private static NetworkUtils.NetInfo sCurNetworkInfo;
    private static final List<NetworkListener> sNetworkListeners = Collections.synchronizedList(new ArrayList());
    private static NetworkUtils.ConnectBroadcastReceiver sConnectBroadcastReceiver;

    public NetworkUtils() {
    }

    public static boolean isNetworkAvailable() {
        return getNetworkType() != -1;
    }

    public static boolean isWifi() {
        return 1 == getNetworkType();
    }

    public static boolean is4G() {
        return 4 == getNetworkType();
    }

    public static boolean isUsingMobileNetwork() {
        int networkType = getNetworkType();
        return networkType == 2 || networkType == 3 || networkType == 4;
    }

    public static boolean isCMWAP() {
        String currentAPN = "";
        ConnectivityManager conManager = (ConnectivityManager) AppModel.getApplication().getSystemService("connectivity");
        NetworkInfo info = conManager.getNetworkInfo(0);
        currentAPN = info.getExtraInfo();
        if (currentAPN != null && currentAPN != "") {
            return "cmwap".equals(currentAPN.toLowerCase());
        } else {
            return false;
        }
    }

    public static boolean isChinaMobile() {
        NetworkUtils.NetInfo networkInfo = getNetworkInfo();
        return "中国移动".equals(networkInfo.operatorName) || "CMCC".equals(networkInfo.operatorName.toUpperCase());
    }

    public static String getNetworkTypeString() {
        int type = getNetworkType();
        return typeIntToString(type);
    }

    public static String getGeneration() {
        int networkType = getNetworkType();
        if (2 == networkType) {
            return "2G";
        } else if (3 == networkType) {
            return "3G";
        } else if (4 == networkType) {
            return "4G";
        } else {
            return 1 == networkType ? "Wifi" : "";
        }
    }

    public static int getNetworkType() {
        return getNetworkInfo().type;
    }

    private static String typeIntToString(int type) {
        switch(type) {
        case 0:
            return "?";
        case 1:
            return "WiFi";
        case 2:
            return "2G";
        case 3:
            return "3G";
        case 4:
            return "4G";
        default:
            return "?";
        }
    }

    public static NetworkUtils.NetInfo getNetworkInfo() {
        Class var0 = NetworkUtils.class;
        synchronized(NetworkUtils.class) {
            if (sCurNetworkInfo == null) {
                try {
                    updateNetworkInfo();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }
        }

        return sCurNetworkInfo;
    }

    public static String getWiFiSSID() {
        WifiManager mWifi = (WifiManager) AppModel.getApplication().getSystemService("wifi");
        if (mWifi != null) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        }

        return null;
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var2 = interfaces.iterator();

            NetworkInterface intf;
            do {
                if (!var2.hasNext()) {
                    return "";
                }

                intf = (NetworkInterface)var2.next();
            } while(interfaceName != null && !intf.getName().equalsIgnoreCase(interfaceName));

            byte[] mac = intf.getHardwareAddress();
            if (mac == null) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder();

                for(int idx = 0; idx < mac.length; ++idx) {
                    buf.append(String.format("%02X:", mac[idx]));
                }

                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }

                return buf.toString();
            }
        } catch (Exception var7) {
            return "";
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var2 = interfaces.iterator();

            while(var2.hasNext()) {
                NetworkInterface intf = (NetworkInterface)var2.next();
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                Iterator var5 = addrs.iterator();

                while(var5.hasNext()) {
                    InetAddress addr = (InetAddress)var5.next();
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(58) < 0;
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else if (!isIPv4) {
                            int delim = sAddr.indexOf(37);
                            return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception var10) {
            ;
        }

        return "";
    }

    public static int getDownLoadBuffer() {
        if (getNetworkType() == 1) {
            return 32768;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager)AppModel.getApplication().getSystemService("phone");
            switch(telephonyManager.getNetworkType()) {
            case 0:
                return 8192;
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 8192;
            case 3:
            case 5:
            case 6:
            case 10:
                return 16384;
            case 8:
            case 9:
            case 14:
                return 16384;
            case 12:
            case 13:
            case 15:
                return 32768;
            default:
                return 8192;
            }
        }
    }

    private static void updateNetworkInfo() {
        NetworkUtils.NetInfo netInfo = new NetworkUtils.NetInfo();
        TelephonyManager telephonyManager = (TelephonyManager)AppModel.getApplication().getSystemService("phone");
        String simOperatorName = telephonyManager.getSimOperatorName();
        netInfo.operatorName = simOperatorName;
        ConnectivityManager connManager = (ConnectivityManager)AppModel.getApplication().getSystemService("connectivity");
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            int networkType = -1;
            int type = info.getType();
            int subType = getSubType(info);
            if (type == 1) {
                networkType = 1;
            } else if (type == 0) {
                switch(subType) {
                case 0:
                case 16:
                default:
                    networkType = 0;
                    break;
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    networkType = 2;
                    break;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                case 17:
                case 18:
                    networkType = 3;
                    break;
                case 13:
                    networkType = 4;
                }
            }

            netInfo.type = networkType;
            netInfo.subType = subType;
            netInfo.extra = info.getExtraInfo();
        } else {
            netInfo.type = -1;
            netInfo.subType = -1;
        }

        sCurNetworkInfo = netInfo;
    }

    private static int getSubType(NetworkInfo info) {
        int subType = 0;

        try {
            subType = info.getSubtype();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return subType;
    }

    private static void notifyNetworkListeners() {
        Iterator iterator = sNetworkListeners.iterator();

        while(iterator.hasNext()) {
            NetworkUtils.NetworkListener listener = (NetworkUtils.NetworkListener)iterator.next();
            if (listener != null) {
                listener.onNetworkStatusChanged(sCurNetworkInfo.type != -1, sCurNetworkInfo);
            } else {
                iterator.remove();
            }
        }

    }

    public static void init(Context context) {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        sConnectBroadcastReceiver = new NetworkUtils.ConnectBroadcastReceiver();
        ContextUtils.registerReceiver(context, sConnectBroadcastReceiver, filter);
    }

    public static void release(Context context) {
        if (sConnectBroadcastReceiver != null) {
            ContextUtils.unregisterReceiver(context, sConnectBroadcastReceiver);
            sConnectBroadcastReceiver = null;
        }

    }

    public static void addNetworkListener(NetworkUtils.NetworkListener listener) {
        sNetworkListeners.add(listener);
        listener.onNetworkStatusChanged(isNetworkAvailable(), getNetworkInfo());
    }

    public static void removeNetworkListener(NetworkUtils.NetworkListener listener) {
        sNetworkListeners.remove(listener);
    }

    public static class NetInfo {
        public int type;
        public int subType;
        public String extra;
        public String operatorName;

        public NetInfo() {
        }
    }

    public interface NetworkListener {
        void onNetworkStatusChanged(boolean var1, NetworkUtils.NetInfo var2);
    }

    public static class ConnectBroadcastReceiver extends BroadcastReceiver {
        public ConnectBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Class var3 = NetworkUtils.class;
                synchronized(NetworkUtils.class) {
                    try {
                        NetworkUtils.updateNetworkInfo();
                        NetworkUtils.notifyNetworkListeners();
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }
                }
            }

        }
    }
}
