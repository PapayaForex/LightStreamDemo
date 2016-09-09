package com.allen.lightstreamdemo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.allen.lightstreamdemo.R;
import com.allen.lightstreamdemo.base.BaseDialogFragment;


/**
 * 作者: allen on 16/6/30.
 */
public class LoadingFragment extends BaseDialogFragment {
    public String Tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tag = getArguments().getString("LoadingFragment");
    }

    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Dialog dialog = new Dialog(getActivity(), R.style.MyDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_loading, null);
        TextView textView = (TextView) view.findViewById(R.id.tipTextView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        // 设置宽度为屏宽、靠近屏幕底部。
        WindowManager manager = getActivity().getWindowManager();
        Window window = dialog.getWindow();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (!TextUtils.isEmpty(Tag)){
            textView.setText(Tag);
        }
        window.setAttributes(wlp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.MyDialogStyle;
    }
}
