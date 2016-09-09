package com.allen.lightstreamdemo;

import android.view.View;
import android.widget.ListView;

import com.allen.lightstreamdemo.adapter.StocksAdapter;
import com.allen.lightstreamdemo.base.Constant;
import com.lightstreamer.client.ItemUpdate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
        if (newData.isValueChanged(Constant.BID)) {
            stockName = newData.getValue(Constant.BID);
            stockNameColor = isSnapshot ? R.color.snapshot_highlight : R.color.higher_highlight;
        }
        if (newData.isValueChanged(Constant.BID)) {
            time = newData.getValue(Constant.BID);
            timeColor = isSnapshot ? R.color.snapshot_highlight : R.color.higher_highlight;
        }
        if (newData.isValueChanged(Constant.OFFER)) {
            double newPrice = Double.parseDouble(newData.getValue(Constant.OFFER));
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
        holder.getStock_name().setText(stockName);
        holder.getLast_price().setText(lastPrice);
        holder.getTime().setText(time);
        this.fillColor(holder);
    }

    private void fillColor(StocksAdapter.RowHolder holder) {
        holder.getStock_name().setBackgroundResource(stockNameColor);
        holder.getLast_price().setBackgroundResource(lastPriceColor);
        holder.getTime().setBackgroundResource(timeColor);
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
