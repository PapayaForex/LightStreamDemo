package com.allen.lightstreamdemo.base;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.allen.lightstreamdemo.fragment.LoadingFragment;


/**
 * 作者: allen on 16/6/23.
 */
public class BaseDialogFragment extends DialogFragment {
    private Toast mToast;
    public DialogFragment mLoadingFragment;
    public Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = new Bundle();


    }

    /**
     * 加载loading样式
     */
    protected void showLoading() {
        if (mLoadingFragment==null){
            mLoadingFragment = new LoadingFragment();
        }
        mLoadingFragment.setArguments(mBundle);
        mLoadingFragment.show(getFragmentManager(),"");


    }

    protected void showLoading(String msg) {
        if (mLoadingFragment==null){
            mLoadingFragment = new LoadingFragment();
        }
        mBundle.putString("LoadingFragment",msg);
        mLoadingFragment.setArguments(mBundle);
        mLoadingFragment.show(getFragmentManager(),msg);
    }


    protected void hideLoading() {
        if (null != mLoadingFragment) {
            mLoadingFragment.dismiss();
        }
    }


    /**
     * Toast弹窗
     *
     * @param message
     */
    protected void showToast(CharSequence message) {
        if (getActivity() != null) {
            if (!TextUtils.isEmpty(message)) {
                if (mToast == null) {
                    mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }

    }
    protected void showAlertDialog(String sms) {
//        Utils.showAlertDialog(sms, getActivity());


    }
    public void ShowSnack(String sms) {
        Snackbar.make(getDialog().getCurrentFocus(), sms, Snackbar.LENGTH_SHORT).show();
    }

}
