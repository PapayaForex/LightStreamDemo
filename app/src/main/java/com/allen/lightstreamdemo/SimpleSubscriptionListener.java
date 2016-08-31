package com.allen.lightstreamdemo;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;
import com.orhanobut.logger.Logger;

/**
 * Created by: allen on 16/8/29.
 */

public class SimpleSubscriptionListener implements SubscriptionListener {
    private String TAG;

    public SimpleSubscriptionListener(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public void onClearSnapshot(String s, int i) {
        Logger.d( "onClearSnapshot: ");
    }

    @Override
    public void onCommandSecondLevelItemLostUpdates(int i, String s) {
        Logger.wtf( "Not expecting 2nd level events");
    }

    @Override
    public void onCommandSecondLevelSubscriptionError(int i, String s, String s1) {
        Logger.wtf("Not expecting 2nd level events");
    }

    @Override
    public void onEndOfSnapshot(String itemName, int i) {
        Logger.v( "Snapshot end for " + itemName);
    }

    @Override
    public void onItemLostUpdates(String s, int i, int i1) {
        Logger.v( "Not expecting lost updates");
    }

    @Override
    public void onItemUpdate(ItemUpdate itemUpdate) {
    }

    @Override
    public void onListenEnd(Subscription subscription) {
        Logger.d( "Stop listening");
    }

    @Override
    public void onListenStart(Subscription subscription) {
        Logger.d( "Start listening");
    }

    @Override
    public void onSubscription() {
        Logger.v( "Subscribed");
    }

    @Override
    public void onSubscriptionError(int i, String message) {
        Logger.e( "Subscription error " + i + ": " + message);
    }

    @Override
    public void onUnsubscription() {
        Logger.v( "Unsubscribed");
    }
}
