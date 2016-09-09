package com.allen.lightstreamdemo.mvp.login;

/**
 * Created by: allen on 16/9/1.
 */

public interface LoginView {
    void showProgress(boolean isShow);

    void showAlertMsg(String watchList);

    void gotoMainActivity();
}
