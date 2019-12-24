package com.hisunfly.one.component.exception;


import com.hisunfly.one.component.utils.NetworkUtils;


import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ExceptionFactory {
    public static AppException create(Throwable t) {
        if (t != null) {
            if (!NetworkUtils.isNetworkAvailable() || t instanceof ConnectException) {
                return new NetworkNoConnException("当前网络不可用，请检查你的网络设置~");
            } else if (t instanceof SocketTimeoutException || t instanceof
                    SocketException) {
                return new NetworkTimeOutException("请求超时~", t);
            } else if(t instanceof AppException) {
                return (AppException) t;
            } else {
                return new AppException("未知错误~", t);
            }
        }
        return new AppException();
    }
}
