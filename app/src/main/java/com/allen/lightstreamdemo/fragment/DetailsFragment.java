package com.allen.lightstreamdemo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.lightstreamdemo.Chart;
import com.allen.lightstreamdemo.R;
import com.allen.lightstreamdemo.Stock;
import com.allen.lightstreamdemo.base.SubscriptionFragment;
import com.allen.lightstreamdemo.base.Constant;
import com.androidplot.xy.XYPlot;
import com.lightstreamer.client.Subscription;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: allen on 16/8/30.
 */

public class DetailsFragment extends Fragment {
    public final static Set<String> numericFields = new HashSet<String>() {{
        add("last_price");
        add("pct_change");
        add("bid_quantity");
        add("bid");
        add("ask");
        add("ask_quantity");
        add("min");
        add("max");
        add("open_price");
    }};
    public final static String[] subscriptionFields = {"stock_name", "last_price", "timestamp", "pct_change", "bid_quantity", "bid", "ask", "ask_quantity", "min", "max", "open_price"};


    private final SubscriptionFragment mSubscriptionFragment = new SubscriptionFragment();
    private Handler mHandler;
    HashMap<String, TextView> holder = new HashMap<>();
    Chart mChart;
    public static final String ARG_ITEM = "item";
    public static final String ARG_PN_CONTROLS = "pn_controls";

    int currentItem = 0;
    private Subscription currentSubscription = null;
    private Stock stockListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        Logger.d( "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentItem = savedInstanceState.getInt(ARG_ITEM);
        }
        View view = inflater.inflate(R.layout.details_view, container, false);
        holder.put("stock_name", (TextView) view.findViewById(R.id.d_stock_name));
        holder.put("last_price", (TextView) view.findViewById(R.id.d_last_price));
        holder.put("timestamp", (TextView) view.findViewById(R.id.d_time));
        holder.put("pct_change", (TextView) view.findViewById(R.id.d_pct_change));
        holder.put("bid_quantity", (TextView) view.findViewById(R.id.d_bid_quantity));
        holder.put("bid", (TextView) view.findViewById(R.id.d_bid));
        holder.put("ask", (TextView) view.findViewById(R.id.d_ask));
        holder.put("ask_quantity", (TextView) view.findViewById(R.id.d_ask_quantity));
        holder.put("min", (TextView) view.findViewById(R.id.d_min));
        holder.put("max", (TextView) view.findViewById(R.id.d_max));
        holder.put("open_price", (TextView) view.findViewById(R.id.d_open_price));
        final XYPlot plot = (XYPlot) view.findViewById(R.id.mySimpleXYPlot);
        mChart = new Chart(plot, mHandler);
        stockListener = new Stock(numericFields, subscriptionFields, mHandler, holder);
        Logger.d("onCreateView: ");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        Logger.d("onStart: "+args);
        if (args != null) {
            updateStocksView(args.getInt(ARG_ITEM));
        } else if (currentItem != 0) {
            updateStocksView(currentItem);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mChart.onPause();
        this.mSubscriptionFragment.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mChart.onResume(this.getActivity());
        this.mSubscriptionFragment.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mSubscriptionFragment.onAttach(activity);
    }
    public void updateStocksView(int item) {
        if (item != currentItem || this.currentSubscription == null) {
            if (this.currentSubscription != null) {
                this.currentSubscription.removeListener(stockListener);
                this.currentSubscription.removeListener(mChart);
            }
            String itemName = "item" + item;

            this.currentSubscription = new Subscription(Constant.MERGE, itemName, subscriptionFields);
//            currentSubscription.setDataAdapter(Constant.QUOTE_ADAPTER);
            this.currentSubscription.setRequestedSnapshot(Constant.YES);
            this.currentSubscription.addListener(stockListener);
            this.currentSubscription.addListener(mChart);
            this.mSubscriptionFragment.setSubscription(this.currentSubscription);
            currentItem = item;
        }

    }

    public int getCurrentStock(){
        return this.currentItem;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_ITEM,currentItem);
    }
}
