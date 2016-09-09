package com.allen.lightstreamdemo.base;

/**
 * 作者: allen on 16/5/6.
 */
public interface BasePresenter<V> {
    void attachView(V view);

    void detachView();
}
