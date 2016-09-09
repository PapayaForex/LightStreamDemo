package com.allen.lightstreamdemo.mvp.login;

import com.allen.lightstreamdemo.BuildConfig;
import com.allen.lightstreamdemo.LightStreamApplication;
import com.allen.lightstreamdemo.base.BasePresenterImpl;
import com.allen.lightstreamdemo.bean.Session;
import com.allen.lightstreamdemo.bean.UserInfo;
import com.allen.lightstreamdemo.bean.WatchList;
import com.allen.lightstreamdemo.callback.NetCallback;
import com.allen.lightstreamdemo.callback.SubscriberCallback;
import com.allen.lightstreamdemo.retrofit.ApiFactory;
import com.allen.lightstreamdemo.service.StreamService;
import com.allen.lightstreamdemo.util.GsonUtils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by: allen on 16/9/1.
 */

public class LoginPresenter extends BasePresenterImpl<LoginView> {
    private StreamService mStreamService = ApiFactory.buildRetrofit().create(StreamService.class);

    public LoginPresenter(LoginView view) {
        super(view);
    }


    public void login(String username, String password) {
        UserInfo userinfo = new UserInfo();
        userinfo.setPassword(BuildConfig.UserName);
        userinfo.setIdentifier(BuildConfig.Password);

        final String key = BuildConfig.ApplicationKey;
        mStreamService.logging("2", key, userinfo).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                Session session = response.body();
                Logger.d(response.headers());
                LightStreamApplication.getInstance().initLightClient(session.getLightstreamerEndpoint(),response.headers().get("CST"),response.headers().get("X-SECURITY-TOKEN"));
                View.gotoMainActivity();

            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });



    }


    public void watchList(final String Key,final  String cst,final String token){
        addSubscription(mStreamService.watchlists(Key,cst,token),new SubscriberCallback(new NetCallback<WatchList>() {
            @Override
            public void onSuccess(WatchList watchList) {
                Logger.d("onSuccess: "+watchList);
                try {
                    final String list = GsonUtils.getInstance().parse(watchList);
                    Logger.d("onSuccess: "+list);
                    View.showAlertMsg(list);
                } catch (Exception e) {
                    Logger.e("onSuccess: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailer(int code, String msg) {
                View.showAlertMsg(msg);
            }

            @Override
            public void onCompleted() {
                Logger.d("onCompleted: ");
                account(Key,cst,token);
            }
        }));
    }

    public void account(final String Key,final String cst, final String token) {
        addSubscription(mStreamService.accounts(Key,cst,token),new SubscriberCallback(new NetCallback<WatchList>() {
            @Override
            public void onSuccess(WatchList watchList) {
                Logger.d("onSuccess: "+watchList);
                try {
                    final String list = GsonUtils.getInstance().parse(watchList);
                    Logger.d("onSuccess: "+list);
                    View.showAlertMsg(list);
                } catch (Exception e) {
                    Logger.e("onSuccess: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailer(int code, String msg) {
                View.showAlertMsg(msg);
            }

            @Override
            public void onCompleted() {
                Logger.d("onCompleted: ");
                activity(Key,cst,token);
            }
        }));
    }
    public void activity(String Key,String cst, String token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        addSubscription(mStreamService.activity("3",Key,cst,token,dateFormat.format(System.currentTimeMillis())),new SubscriberCallback(new NetCallback<WatchList>() {
            @Override
            public void onSuccess(WatchList watchList) {
                Logger.d("onSuccess: "+watchList);
                try {
                    final String list = GsonUtils.getInstance().parse(watchList);
                    Logger.d("onSuccess: "+list);
                    View.showAlertMsg(list);
                } catch (Exception e) {
                    Logger.e("onSuccess: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailer(int code, String msg) {
                View.showAlertMsg(msg);
            }

            @Override
            public void onCompleted() {
                Logger.d("onCompleted: ");
            }
        }));
    }

}
