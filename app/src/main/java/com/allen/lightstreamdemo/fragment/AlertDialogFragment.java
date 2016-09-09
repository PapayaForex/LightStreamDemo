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
import com.allen.lightstreamdemo.common.DialogCallBack;


/**
 * 作者: allen on 16/7/13.
 */
public class AlertDialogFragment extends BaseDialogFragment {
    public String sms;
    public String tip;
    public DialogCallBack dialogCallBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sms = getArguments().getString("sms");
        tip = getArguments().getString("tip");
    }
    public void  setDialogCallBack(DialogCallBack dialogCallBack){
        this.dialogCallBack =dialogCallBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
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
        if (!TextUtils.isEmpty(tip)){
            textView.setText(tip);
        }
        window.setAttributes(wlp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView btn = (TextView) view.findViewById(R.id.btn);
        dialog.setContentView(view);
        title.setText("提示");
        message.setText(sms);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallBack!=null){
                    dialogCallBack.call();
                }
                dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.MyDialog;
    }
}

