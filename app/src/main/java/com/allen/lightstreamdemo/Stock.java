package com.allen.lightstreamdemo;

import android.os.Handler;
import android.widget.TextView;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by: allen on 16/8/30.
 */

public class Stock extends SimpleSubscriptionListener {
    private final String[] fields;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private HashMap<String, TextView> holder = null;
    private HashMap<String, UpdateRunnable> turnOffRunnables = new HashMap<>();
    Set<String> numericField;
    private Handler handler;
    private Subscription mSubscription;

    public Stock(Set<String> numericFields, String[] fields, Handler handler, HashMap<String, TextView> holder) {
        super("Stock");

        this.fields = fields;
        this.numericField = numericFields;

        this.handler = handler;
        this.holder = holder;
    }

    @Override
    public void onListenStart(Subscription subscription) {
        super.onListenStart(subscription);
        this.mSubscription = subscription;
        handler.post(new ResetRunnable());
    }

    @Override
    public void onListenEnd(Subscription subscription) {
        super.onListenEnd(subscription);
        this.mSubscription = null;
    }

    @Override
    public void onItemUpdate(ItemUpdate itemUpdate) {
        super.onItemUpdate(itemUpdate);
        this.updateView(itemUpdate);
    }

    private void updateView(ItemUpdate newData) {
        boolean snapshot = newData.isSnapshot();
        String itemName = newData.getItemName();
        Iterator<Map.Entry<String, String>> changedFields = newData.getChangedFields().entrySet().iterator();
        while (changedFields.hasNext()) {
            Map.Entry<String, String> updatedField = changedFields.next();
            String value = updatedField.getValue();
            String fieldName = updatedField.getKey();
            TextView field = holder.get(fieldName);
            if (field != null) {
                if (fieldName.equals(Constant.TIMESTAMP)) {
                    Date then = new Date(Long.parseLong(value));
                    value = mSimpleDateFormat.format(then);
                }
                double upDown = 0.0;
                int color;
                if (!snapshot) {
                    if (numericField.contains(fieldName)) {
                        try {
                            String oldValue = mSubscription.getValue(itemName, fieldName);
                            double valueNum = Double.parseDouble(value);
                            double oldValueNum = Double.parseDouble(oldValue);
                            upDown = valueNum - oldValueNum;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    if (upDown < 0) {
                        color = R.color.lower_highlight;
                    } else {
                        color = R.color.higher_highlight;
                    }
                } else {
                    color = R.color.lower_highlight;
                }
                UpdateRunnable turnOff = turnOffRunnables.get(fieldName);
                if (turnOff != null) {
                    turnOff.invalidate();
                }
                turnOff = new UpdateRunnable(R.color.transparent, field, null);
                this.turnOffRunnables.put(fieldName, turnOff);
                handler.post(new UpdateRunnable(color, field, value));
                handler.postDelayed(turnOff, 600);
            }
        }
    }

    private class ResetRunnable implements Runnable {
        @Override
        public synchronized void run() {

            resetHolder(holder, fields);
        }

        private void resetHolder(HashMap<String, TextView> holder, String[] fields) {
            for (int i = 0; i < fields.length; i++) {
                TextView field = holder.get(fields[i]);
                if (field != null) {
                    field.setText("N/A");
                }
            }
        }
    }

    private class UpdateRunnable implements Runnable {
        private int background;
        private TextView mTextView;
        private String text;
        private boolean valid = true;

        public UpdateRunnable(int background, TextView textView, String text) {
            this.background = background;
            mTextView = textView;
            this.text = text;
        }

        @Override
        public synchronized void run() {
            if (this.valid) {
                if (this.text != null) {
                    mTextView.setText(text);
                }
                mTextView.setBackgroundResource(background);
                mTextView.invalidate();
            }
        }

        public synchronized void invalidate() {
            this.valid = false;
        }
    }
}
