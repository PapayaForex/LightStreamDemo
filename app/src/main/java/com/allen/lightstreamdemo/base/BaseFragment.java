package com.allen.lightstreamdemo.base;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.allen.lightstreamdemo.fragment.LoadingFragment;

import java.util.List;

//import com.squareup.leakcanary.RefWatcher;

/**
 * Created by mamaolu on 15-5-21.
 */
public class BaseFragment extends Fragment {
//    private Dialog dlg_loading = null;
    private Toast mToast;
    AlertDialog dialog;
    protected final String TAG = this.getClass().getSimpleName();
    private long backmill = 0;
    private long currentmill;
    protected Context context;
    public DialogFragment mLoadingFragment;
    public Bundle mBundle;
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

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
        mLoadingFragment.show(getActivity().getFragmentManager(),"");


    }

    protected void showLoading(String msg) {
        if (mLoadingFragment==null){
            mLoadingFragment = new LoadingFragment();
        }
        mBundle.putString("LoadingFragment",msg);
        mLoadingFragment.setArguments(mBundle);
        mLoadingFragment.show(getActivity().getFragmentManager(),msg);
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
        if (context != null) {
            if (!TextUtils.isEmpty(message)) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }

    }

    /**
     * 联网失败
     */
    protected void ShowFailure() {
        showToast("联网失败,请检查您的网络");
    }

    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void showAlertDialog(String sms) {
//        Utils.showAlertDialog(sms, context);


    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }


        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm =getFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                Log.w(TAG, "Activity result fragment index out of range: 0x"
                        + Integer.toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
}

