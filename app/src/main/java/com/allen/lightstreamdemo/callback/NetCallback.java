package com.allen.lightstreamdemo.callback;

/**
 * Created by: allen on 16/8/11.
 */

public interface NetCallback<T> {
    void onSuccess(T mode);

    void onFailer(int code, String msg);

    void onCompleted();
}
