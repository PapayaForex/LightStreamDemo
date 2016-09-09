package com.allen.lightstreamdemo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DialogFragment;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.lightstreamdemo.common.DialogCallBack;
import com.allen.lightstreamdemo.fragment.AlertDialogFragment;
import com.allen.lightstreamdemo.fragment.LoadingFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Allen on 15-5-21.
 */
public abstract class BaseMvpActivity<P extends BasePresenterImpl> extends AppCompatActivity {
    protected TextView tvedit;
    protected TextView mActionBarTitle;
    protected View customView;
    public TextView tv_right;
    private WeakReference<Activity> context = null;
    protected P Presenrer;
    protected CompositeSubscription mCompositeSubscription;
    public AlertDialogFragment mAlertDialogFragment;
    public View getCustomView() {
        return customView;
    }

    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 针对部分机型设置超大字体等特殊字体后 ，屏幕显示UI变形情况
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }


    protected TextView mBack;

    public TextView getmBack() {
        return mBack;
    }

    public void setmBack(TextView mBack) {
        this.mBack = mBack;
    }

    private Toast mToast;
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();
    public DialogFragment mLoadingFragment;
    public Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Presenrer = createPresenter();
        initActionBar();

        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mBundle = new Bundle();
        // 设置android:fitsSystemWindows="true"属性
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 19) {
            parentView.setFitsSystemWindows(true);
        }
//        setStatusBarColor(R.color.white);
    }

    protected abstract P createPresenter();


    protected void setStatusBarColor(@DrawableRes int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
////            tintManager.setTintColor(Color.TRANSPARENT);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().setBackgroundDrawableResource(color);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected void initActionBar() {
    }

    /**
     * 加载loading样式
     */
    protected void showLoading() {
        if (mLoadingFragment == null) {
            mLoadingFragment = new LoadingFragment();
        }
        mBundle.putString("LoadingFragment", "");
        if (mLoadingFragment.getArguments() != null) {
            mLoadingFragment.getArguments().putAll(mBundle);
        } else {
            mLoadingFragment.setArguments(mBundle);
        }

        mLoadingFragment.show(getFragmentManager(), "");


    }

    protected void showLoading(String msg) {
        if (mLoadingFragment == null) {
            mLoadingFragment = new LoadingFragment();
        }
        mBundle.putString("LoadingFragment", msg);
        if (mLoadingFragment.getArguments() != null) {
            mLoadingFragment.getArguments().putAll(mBundle);
        } else {
            mLoadingFragment.setArguments(mBundle);
        }
        mLoadingFragment.show(getFragmentManager(), msg);
    }

    protected void hideLoading() {
        if (null != mLoadingFragment) {
            mLoadingFragment.dismiss();
        }
    }


    @Override
    public void finish() {
        super.finish();
    }

    /**
     * actionbar标题
     *
     * @param title
     */
    protected void setTitle(@NonNull String title) {
        if (null != mActionBarTitle) {
            mActionBarTitle.setText(title);
        }
    }

    public TextView getmActionBarTitle() {
        return mActionBarTitle;
    }


    /**
     * actionbar回退键
     */
    protected void setBackBtn() {
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setBackBtn(View.OnClickListener listener) {
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(listener);
    }

    /**
     * 联网失败
     */
    protected void ShowFailure() {
        showToast("联网失败,请检查您的网络");
    }


    protected void showToast(CharSequence message) {
        if (!this.isFinishing()) {
            if (!TextUtils.isEmpty(message)) {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }
    }

    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }

    private void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }

    protected void showAlertDialog(String sms) {
        if (this.isFinishing()){
            return;
        }
        if (mAlertDialogFragment ==null){
            mAlertDialogFragment = new AlertDialogFragment();
        }
        if (mAlertDialogFragment.isAdded()){
            return;
        }
        mBundle.putString("sms",sms);
        if (mAlertDialogFragment.getArguments()!=null){
            mAlertDialogFragment.getArguments().putAll(mBundle);
        }else {
            mAlertDialogFragment.setArguments(mBundle);
        }
        if (mAlertDialogFragment.isVisible()){
            mAlertDialogFragment.dismiss();
        }
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(mAlertDialogFragment, "mAlertDialogFragment");
        ft.commitAllowingStateLoss();
//        mAlertDialogFragment.show(getFragmentManager(),"mAlertDialogFragment");
    }

    protected void showAlertDialog(String sms, DialogCallBack dialogCallBack) {
        if (isFinishing()){
            return;
        }
        if (mAlertDialogFragment ==null){
            mAlertDialogFragment = new AlertDialogFragment();
        }
        if (mAlertDialogFragment.isAdded()){
            return;
        }
        mBundle.putString("sms",sms);
        if (mAlertDialogFragment.getArguments()!=null){
            mAlertDialogFragment.getArguments().putAll(mBundle);
        }else {
            mAlertDialogFragment.setArguments(mBundle);
        }
        if (mAlertDialogFragment.isVisible()){
            mAlertDialogFragment.dismiss();
        }
        mAlertDialogFragment.setDialogCallBack(dialogCallBack);
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(mAlertDialogFragment, "mAlertDialogFragment");
        ft.commitAllowingStateLoss();
//        mAlertDialogFragment.show(getFragmentManager(),"mAlertDialogFragment");
    }

    /*
     * 退出程序 即所有activity finish掉
	 */
    public void exist() {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {

                activity.finish();
//                MainApplication_.getInstance().removeTask(activity);
            }
            //android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

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

    private boolean isSleeping() {
        KeyguardManager kgMgr = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {

        super.onStop();
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
