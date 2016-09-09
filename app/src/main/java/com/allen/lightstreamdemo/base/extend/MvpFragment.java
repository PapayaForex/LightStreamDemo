package com.allen.lightstreamdemo.base.extend;

import android.os.Bundle;
import android.view.View;

import com.allen.lightstreamdemo.base.BaseFragment;
import com.allen.lightstreamdemo.base.BasePresenterImpl;


/**
 * Created by: allen on 16/8/11.
 */

public abstract class MvpFragment<P extends BasePresenterImpl> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
