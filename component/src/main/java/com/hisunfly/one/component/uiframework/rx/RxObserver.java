package com.hisunfly.one.component.uiframework.rx;

import com.hisunfly.one.component.exception.AppException;
import com.hisunfly.one.component.exception.ExceptionFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Desc:默认rxjava2 订阅者，有对ResponseBean对象进行空值和错误处理
 * <p>
 * Author: jinyuef
 * Date: 2018-06-22
 * Copyright: Copyright (c) 2010-2018
 * Company: @咪咕动漫
 * Updater:
 * Update Time:
 * Update Comments:
 */
public abstract class RxObserver<T> implements Observer<T> {

    protected Disposable mDisposable;
    /**
     * 返回的数据的次序，从0开始
     */
    private int mItemIndex = 0;

    /**
     * 检查ResponseBean的空数据
     */
    private boolean mCheckNullData = false;

    /**
     * 错误计数
     */
    private int mErrorCount;

    /**
     * 检查ResponseBean的data空值
     *
     * @return
     */
    public RxObserver<T> checkNullData() {
        mCheckNullData = true;
        return this;
    }


    /**
     * Desc:创建空的订阅者
     * <p>
     * Author: jinyuef
     * Date: 2018-06-22
     *
     * @return rx observer
     */
    public static RxObserver empty() {
        return new RxObserver() {
            @Override
            protected void onNext(Object o, int itemIndex) {

            }
        };
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        Exception exception = checkError(t);
        try {
            if (exception == null) {
                onNext(t, mItemIndex);
                doOnNext(t, mItemIndex);
                mItemIndex++;
            } else {
                onError(exception);
            }
        } catch (Exception e) {
            onError(e);
//            if (SettingConfig.DEBUG) {
//                throw e;
//            } else {
//                onError(e);
//            }
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            e.printStackTrace();
            AppException exception = ExceptionFactory.create(e);
            onError(exception, mItemIndex, mErrorCount + 1);
            doOnError(exception);
            mItemIndex++;
            mErrorCount++;
        } catch (Exception e1) {
            e1.printStackTrace();
//            if (SettingConfig.DEBUG) {
//                throw e1;
//            }
        } finally {
            onComplete();
        }
    }

    @Override
    public void onComplete() {

    }

    private Exception checkError(T t) {
        if (t == null) {
            return new NullPointerException("数据是空的");
        }
//        if (t instanceof ResponseBean) {
//            ResponseBean resp = (ResponseBean) t;
//            //服务端返回的数据内容是空的
//            if (!resp.isSuccess() || (mCheckNullData && resp.getResult() == null)) {
//                DataException dataException = new DataException(resp.getCode(), resp.getCode() ==
//                        ResponseBean.RESULT_CODE_TOKEN_EXPIRE ? null : resp
//                        .getShowUserMsg(),
//                        resp.getResult());
//                /**
//                 * 重新获取token
//                 */
//                if (resp.getCode() == ResponseBean.RESULT_CODE_TOKEN_EXPIRE) {
//                    TokenExpiredManager.getInstance().requestNewToken();
//                }
//                return dataException;
//            }
//        }
        return null;
    }

    protected void doOnNext(T t, int mItemIndex) {
    }

    protected abstract void onNext(T t, int itemIndex);

    protected void doOnError(AppException e) {
    }

    /**
     * 错误回调
     *
     * @param e          错误信息
     * @param itemIndex  本次数据错误的次序
     * @param errorCount 目前已经出错的总数
     */
    protected void onError(AppException e, int itemIndex, int errorCount) {

    }

    public void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * Desc:是否未处理
     * <p>
     * Author: jinyuef
     * Date: 2018-06-22
     */
    public boolean isNotDisposed() {
        return mDisposable != null && !mDisposable.isDisposed();
    }
}
