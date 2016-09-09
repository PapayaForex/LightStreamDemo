package com.allen.lightstreamdemo.base;

import android.app.Activity;

import com.allen.lightstreamdemo.ILightStreamerClientProxy;
import com.allen.lightstreamdemo.LightStreamApplication;
import com.lightstreamer.client.Subscription;
import com.orhanobut.logger.Logger;

/**
 * Created by: allen on 16/8/30.
 */

public class SubscriptionFragment {
    private static final String TAG = "SubscriptionFragment";
    private Subscription mSubscription;
    private ILightStreamerClientProxy lsClient;
    private boolean subscribed = false;
    private boolean running = false;


    public synchronized void setSubscription(Subscription subscription) {
        if (this.mSubscription != null && subscribed) {
            Logger.d("Replacing subscription");
            this.lsClient.removeSubscription(this.mSubscription);
        }
        Logger.d( "New Subscription" + subscription);
        this.mSubscription = subscription;
        if (running) {
            this.lsClient.addSubscription(this.mSubscription);
        }
    }

    public synchronized void onResume() {
        if (this.lsClient != null && this.mSubscription != null) {
            this.lsClient.addSubscription(this.mSubscription);
            subscribed = true;
        }
        running = true;
    }

    public synchronized void onPause() {
        if (this.lsClient != null && this.mSubscription != null) {
            this.lsClient.removeSubscription(this.mSubscription);
            subscribed = false;
        }
        running = false;
    }

    public synchronized void onAttach(Activity activity) {
        lsClient = LightStreamApplication.sClientProxy;
    }
}
