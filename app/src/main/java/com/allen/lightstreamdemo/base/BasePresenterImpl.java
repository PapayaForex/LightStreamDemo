package com.allen.lightstreamdemo.base;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by: allen on 16/8/11.
 */

public class BasePresenterImpl<V> implements BasePresenter<V> {
    public V View;

    private CompositeSubscription mCompositeSubscription;

    public BasePresenterImpl(V view) {
        View = view;
    }

    @Override
    public void attachView(V view) {
        this.View = view;
    }

    @Override
    public void detachView() {
        this.View = null;
        onUnsubscribe();
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable.
                 subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
