package com.allen.lightstreamdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.orhanobut.logger.Logger;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by: allen on 16/8/30.
 */

public class Chart extends SimpleSubscriptionListener {

    private double maxY = 0;
    private double minY = 0;
    private Series mSeries;
    private XYPlot dynamicPlot;
    private Handler mHandler;
    private final static int MAX_SERIES_SIZE =40;
    private final static String TAG ="Chart";

    public Chart(XYPlot dynamicPlot, Handler handler) {
        super(TAG);
        this.mSeries = new Series();
        this.mHandler = handler;
        this.setPlot(dynamicPlot);
    }
    public void setPlot(XYPlot dynamicPlot) {
        if (this.dynamicPlot!=dynamicPlot){
            this.dynamicPlot =dynamicPlot;
            dynamicPlot.setDomainStep(XYStepMode.SUBDIVIDE, 4);
            dynamicPlot.setRangeStep(XYStepMode.SUBDIVIDE, 5);
            dynamicPlot.getLegendWidget().setVisible(false);

            dynamicPlot.getBackgroundPaint().setColor(Color.BLACK);
            dynamicPlot.getGraphWidget().getBackgroundPaint().setColor(Color.BLACK);
            dynamicPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.BLACK);

            dynamicPlot.getGraphWidget().getDomainLabelPaint().setColor(Color.WHITE);
            dynamicPlot.getGraphWidget().getRangeLabelPaint().setColor(Color.WHITE);

            dynamicPlot.setRangeBoundaries(minY, maxY, BoundaryMode.FIXED);

            dynamicPlot.setDomainValueFormat(new FormatDateLabel());
        }
    }

    public void onResume(Context context){
        PixelUtils.init(context);

        int line = context.getResources().getColor(R.color.chart_line);
        LineAndPointFormatter formatter = new LineAndPointFormatter(line, line, null, null);
        this.dynamicPlot.addSeries(mSeries, formatter);

        this.clean();
    }
    public void onPause() {
        this.dynamicPlot.removeSeries(mSeries);
    }

    private void clean() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG,"Reset chart");
                mSeries.reset();
                maxY =0;
                minY =0;
            }
        });
        this.redraw();
    }

    private void onFirstPoint(double newPrice){
        Logger.d(TAG, "First point on chart");
        minY =newPrice-1;
        if (minY<0){
            minY =0;
        }
        maxY = newPrice+1;
        Logger.d(TAG,"New Y boundaries "+ this.minY+" ->"+this.maxY );
    }
    private void  addPoint(final String time,final String lastPrice){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.v(TAG,"New point");
                mSeries.add(time,lastPrice);
            }
        });
        this.redraw();
    }

    @Override
    public void onListenStart(Subscription subscription) {
        super.onListenStart(subscription);
        this.clean();
    }

    @Override
    public void onItemUpdate(ItemUpdate itemUpdate) {
        super.onItemUpdate(itemUpdate);
        String lastPrice =itemUpdate.getValue(Constant.LAST_PRICE);
        String time =itemUpdate.getValue(Constant.TIMESTAMP);
        this.addPoint(time,lastPrice);
    }

    private void redraw(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dynamicPlot != null) {
                    dynamicPlot.setRangeBoundaries(minY, maxY, BoundaryMode.FIXED);
                    Logger.d("Redraw chart");
                    dynamicPlot.redraw();
                }
            }
        });
    }

    private void onYOverflow(double last) {
        Logger.d(TAG,"Y overflow detected");
        int shift = 1;
        if (last>maxY){
            double newMax = maxY+shift;
            if (last>newMax){
                newMax =last;
            }
            this.maxY = newMax;
        }else if (last<maxY){
            double newMin = minY-shift;
            if (last<newMin){
                newMin =last;
            }
            this.minY = newMin;
        }
        Logger.i(TAG, "New Y boundaries: " + this.minY + " -> "+ this.maxY);
    }

    private class Series implements XYSeries {


        ArrayList<Number> prices = new ArrayList<>();
        ArrayList<Number> times = new ArrayList<>();

        @Override
        public String getTitle() {
            return "";
        }

        public void add(String time, String lastPrice) {
            if (prices.size() >= MAX_SERIES_SIZE) {
                prices.remove(0);
                times.remove(0);
            }

            long longTime = Long.parseLong(time);
            double newPrice = Double.parseDouble(lastPrice);

            if (prices.size() == 0) {
                onFirstPoint(newPrice);
            }

            if (newPrice < minY || newPrice > maxY) {
                onYOverflow(newPrice);
            }

            prices.add(newPrice);
            times.add(longTime);
        }

        public void reset() {
            prices.clear();
            times.clear();
        }

        @Override
        public Number getX(int index) {
            Log.v(TAG,"Extract X");
            return times.get(index);
        }

        @Override
        public Number getY(int index) {
            Log.v(TAG,"Extract Y");
            return prices.get(index);
        }

        @Override
        public int size() {
            Log.v(TAG,"Extract size");
            return prices.size();
        }

    }

    @SuppressWarnings("serial")
    private class FormatDateLabel extends Format {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public StringBuffer format(Object object, StringBuffer buffer,
                                   FieldPosition field) {
            Number num = (Number) object;

            long val = num.longValue();

            Date then = new Date(val);

            buffer.append(dateFormat.format(then));

            return buffer;
        }

        @Override
        public Object parseObject(String string, ParsePosition position) {
            return null;
        }
    }

}
