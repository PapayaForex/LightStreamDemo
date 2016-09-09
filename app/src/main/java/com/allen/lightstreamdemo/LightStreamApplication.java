package com.allen.lightstreamdemo;

import android.app.Application;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by: allen on 16/8/29.
 */

public class LightStreamApplication extends Application {
    public static ILightStreamerClientProxy sClientProxy = null;
    private static LightStreamApplication instance;
    public static LightStreamApplication getInstance() {
        return instance;
    }
    private ArrayList<StockForList> mLists;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        Logger
                .init("TAG")                 // default PRETTYLOGGER or use just init()

        // default 0
                ;
//        sClientProxy = new ClientProxy();
    }

    public  void initLightClient(String host,String client_token,String account_token){
        sClientProxy = new ClientProxyImpl(host,client_token,account_token);
    }


    private class ClientProxyImpl implements ILightStreamerClientProxy {
        private boolean connectionWish = false;
        private boolean userWantsConnection = true;
        private LightstreamerClient lsClient = new LightstreamerClient(null, "DEMO");

        /**
         * init LightStream password and user
         */
        public ClientProxyImpl(String host,String client_token,String account_token) {
            lsClient.connectionDetails.setServerAddress(host);
            lsClient.connectionDetails.setUser(BuildConfig.UserName);
            lsClient.connectionDetails.setPassword("CST-" + client_token + "|XST-" + account_token);
            lsClient.connectionDetails.setAdapterSet("DEFAULT");
            lsClient.connect();
            Logger.d(host,client_token,account_token);
        }

        @Override
        public boolean start(boolean userCall) {
            synchronized (lsClient) {
                if (!userCall) {
                    if (!userWantsConnection) {
                        return false;
                    }
                } else {
                    userWantsConnection = true;
                }
                connectionWish = true;
                lsClient.connect();

                return  true;
            }
        }

        @Override
        public void stop(boolean userCall) {
            synchronized (lsClient) {
                connectionWish = false;
                if (userCall) {
                    userWantsConnection = false;
                    lsClient.disconnect();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            synchronized (lsClient) {
                                if (!connectionWish) {
                                    lsClient.disconnect();
                                }
                            }

                        }
                    }.start();
                }
            }
        }

        @Override
        public void addSubscription(Subscription sub) {
            lsClient.subscribe(sub);
            Logger.d(TAG, "addSubscription: ");
        }

        @Override
        public void removeSubscription(Subscription sub) {
            lsClient.unsubscribe(sub);
            Logger.d(TAG, "removeSubscription: ");
        }

        @Override
        public void addListener(ClientListener listener) {
            lsClient.addListener(listener);
            Logger.d(TAG, "addListener: ");
        }

        @Override
        public void removeListener(ClientListener listener) {
            lsClient.removeListener(listener);
            Logger.d(TAG, "removeListener: ");
        }

        @Override
        public String getStatus() {
            return lsClient.getStatus();
        }
    }
}
