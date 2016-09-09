package com.allen.lightstreamdemo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allen.lightstreamdemo.R;
import com.allen.lightstreamdemo.StockForList;

import java.util.ArrayList;

/**
 * Created by: allen on 16/8/30.
 */

public class StocksAdapter extends ArrayAdapter<StockForList> {
    private Activity mActivity;

    public StocksAdapter(Activity activity, int resource, ArrayList<StockForList> list) {
        super(activity, resource, list);
        this.mActivity =activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RowHolder holder;
        if (row ==null){
            LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            holder = new RowHolder();
            holder.stock_name = (TextView) row.findViewById(R.id.stock_name);
            holder.last_price = (TextView) row.findViewById(R.id.last_price);
            holder.time = (TextView) row.findViewById(R.id.time);
            row.setTag(holder);

        }else {
            holder = (RowHolder) row.getTag();
        }
        StockForList stock = getItem(position);
        stock.fill(holder);
        return row;
    }

    public class RowHolder {
        TextView stock_name;
        TextView last_price;
        TextView time;

        public TextView getStock_name() {
            return stock_name;
        }

        public void setStock_name(TextView stock_name) {
            this.stock_name = stock_name;
        }

        public TextView getLast_price() {
            return last_price;
        }

        public void setLast_price(TextView last_price) {
            this.last_price = last_price;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }
    }
}
