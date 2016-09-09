package com.allen.lightstreamdemo.callback;

import com.orhanobut.logger.Logger;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by: allen on 16/8/11.
 */

public class SubscriberCallback<T > extends Subscriber<T> {
    private final static String TAG = "SubscriberCallback";
    private NetCallback<T> mNetCallback;

    public SubscriberCallback(NetCallback<T> netCallback) {
        this.mNetCallback = netCallback;
    }

    @Override
    public void onCompleted() {
        mNetCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        Logger.d("onError: " + e.getLocalizedMessage());
        int code = 404;
        String msg = "未知错误";
        if (e instanceof HttpException) {
            code = 500;
            msg = "世界最遥远的距离就是没网";
        }
        mNetCallback.onFailer(code, msg);
        mNetCallback.onCompleted();
    }

    @Override
    public void onNext(T t) {
        mNetCallback.onSuccess(t);


    }
}
