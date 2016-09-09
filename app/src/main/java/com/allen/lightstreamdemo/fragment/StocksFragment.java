package com.allen.lightstreamdemo.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.allen.lightstreamdemo.LightStreamApplication;
import com.allen.lightstreamdemo.ILightStreamerClientProxy;
import com.allen.lightstreamdemo.MainSubscription;
import com.allen.lightstreamdemo.R;
import com.allen.lightstreamdemo.StockForList;
import com.allen.lightstreamdemo.adapter.StocksAdapter;
import com.allen.lightstreamdemo.base.Constant;
import com.lightstreamer.client.Subscription;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by: allen on 16/8/29.
 */

public class StocksFragment extends ListFragment {
    onStockSelectedListener mListener;

    public interface onStockSelectedListener {
        void onStockSelected(int item);
    }

//    final static String[] items = {"item100", "item2", "item3",
//            "item4", "item5", "item6", "item7", "item8", "item9", "item10",
//            "item11", "item12", "item13", "item14", "item15", "item16",
//            "item17", "item18", "item19", "item20","item21", "item22", "item23" };
    final static String[] items = {"MARKET:CS.D.CFAGOLD.CFA.IP" };


//    public final static String[] subscriptionFields = {Constant.STOCK_NAME, Constant.LAST_PRICE,Constant.TIMESTAMP};

    public final static String[] subscriptionFields = {Constant.BID, Constant.OFFER};

    private Handler mHandler;
    ILightStreamerClientProxy lsClient;

    private static ArrayList<StockForList> sList;

    static {
        sList = new ArrayList<StockForList>(items.length);
        for (int i = 0; i < items.length; i++) {
            sList.add(new StockForList(i));
        }
    }

    private Subscription mainSubscription = new Subscription(Constant.MERGE, StocksFragment.items, StocksFragment.subscriptionFields);
    private MainSubscription mainSubscriptionListener = new MainSubscription(sList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        setListAdapter(new StocksAdapter(getActivity(), R.layout.row_layout, sList));
    }

    @Override
    public void onStart() {
        super.onStart();
        mainSubscriptionListener.changeContext(mHandler, getListView());
        if (getFragmentManager().findFragmentById(R.id.details_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < sList.size(); i++) {
            StockForList item = sList.get(i);
            item.clean();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lsClient = LightStreamApplication.sClientProxy;
        mainSubscription.setDataAdapter("DEFAULT");
        mainSubscription.setRequestedMaxFrequency(Constant.FRE);
        mainSubscription.setRequestedSnapshot(Constant.YES);
        mainSubscription.addListener(mainSubscriptionListener);
        lsClient.addSubscription(mainSubscription);
        try {
            mListener = (onStockSelectedListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getLocalizedMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lsClient.removeSubscription(mainSubscription);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onStockSelected(position + 1);
        getListView().setItemChecked(position, true);
    }
}
