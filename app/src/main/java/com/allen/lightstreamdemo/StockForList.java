package com.allen.lightstreamdemo;

import android.view.View;
import android.widget.ListView;

import com.lightstreamer.client.ItemUpdate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by: allen on 16/8/29.
 */

public class StockForList {
    private String stockName = "N/A";
    private String lastPrice = "N/A";
    private double lastPriceNum;
    private String time = "N/A";
    private int stockNameColor = R.color.background;
    private int lastPriceColor = R.color.background;
    private int timeColor = R.color.background;
    private int pos;
    private TurnOffRunnable mTurnOffRunnable;
    private DecimalFormat mFormat = new DecimalFormat("#.00");
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");

    public StockForList(int pos) {
        this.pos = pos;
    }

    public void update(ItemUpdate newData, final MainSubscription.Context context) {
        boolean isSnapshot = newData.isSnapshot();
        if (newData.isValueChanged(Constant.STOCK_NAME)) {
            stockName = newData.getValue(Constant.STOCK_NAME);
            stockNameColor = isSnapshot ? R.color.snapshot_highlight : R.color.higher_highlight;
        }
        if (newData.isValueChanged(Constant.TIMESTAMP)) {
            time = mDateFormat.format(new Date(Long.parseLong(newData.getValue(Constant.TIMESTAMP))));
            timeColor = isSnapshot ? R.color.snapshot_highlight : R.color.higher_highlight;
        }
        if (newData.isValueChanged(Constant.LAST_PRICE)) {
            double newPrice = Double.parseDouble(newData.getValue(Constant.LAST_PRICE));
            lastPrice = mFormat.format(newPrice);
            if (isSnapshot) {
                lastPriceColor = R.color.snapshot_highlight;
            } else {
                lastPriceColor = newPrice < lastPriceNum ? R.color.lower_highlight : R.color.higher_highlight;
                lastPriceNum = newPrice;
            }

        }
        if (this.mTurnOffRunnable != null) {
            this.mTurnOffRunnable.disable();
        }
        context.mHandler.post(new Runnable() {
            @Override
            public void run() {
                StocksAdapter.RowHolder holder = extractHolder(context.mListView);
                if (holder != null) {
                    fill(holder);
                }
            }
        });
        this.mTurnOffRunnable = new TurnOffRunnable(context);
        context.mHandler.postDelayed(this.mTurnOffRunnable, 600);
    }

    public void clean() {
        if (this.mTurnOffRunnable != null) {
            this.mTurnOffRunnable.disable();
            this.mTurnOffRunnable = null;
        }
        stockNameColor = R.color.background;
        lastPriceColor = R.color.background;
        timeColor = R.color.background;
    }

    public void fill(StocksAdapter.RowHolder holder) {
        holder.stock_name.setText(stockName);
        holder.last_price.setText(lastPrice);
        holder.time.setText(time);
        this.fillColor(holder);
    }

    private void fillColor(StocksAdapter.RowHolder holder) {
        holder.stock_name.setBackgroundResource(stockNameColor);
        holder.last_price.setBackgroundResource(lastPriceColor);
        holder.time.setBackgroundResource(timeColor);
    }

    StocksAdapter.RowHolder extractHolder(ListView listView) {
        View row = listView.getChildAt(pos - listView.getFirstVisiblePosition());
        if (row == null) {
            return null;
        }
        return (StocksAdapter.RowHolder) row.getTag();
    }

    private class TurnOffRunnable implements Runnable {
        private boolean valid = true;
        private MainSubscription.Context mContext;

        public TurnOffRunnable(MainSubscription.Context context) {
            mContext = context;
        }

        public synchronized void disable() {
            valid = false;
        }

        @Override
        public synchronized void run() {
            if (!valid) {
                return;
            }
            stockNameColor = R.color.transparent;
            lastPriceColor = R.color.transparent;
            timeColor = R.color.transparent;
            StocksAdapter.RowHolder holder = extractHolder(mContext.mListView);
            if (holder != null) {
                fillColor(holder);
            }
        }
    }
}
