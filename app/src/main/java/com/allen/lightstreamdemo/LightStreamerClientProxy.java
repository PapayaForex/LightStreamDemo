package com.allen.lightstreamdemo;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.Subscription;

/**
 * Created by: allen on 16/8/29.
 */

public interface LightStreamerClientProxy {
    boolean start(boolean userCall);

    void stop(boolean userCall);

    void addSubscription(Subscription sub);

    void removeSubscription(Subscription sub);

    void addListener(ClientListener listener);

    void removeListener(ClientListener listener);

    String getStatus();
}
