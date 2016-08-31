package com.allen.lightstreamdemo;

import android.os.Handler;
import android.widget.ListView;

import com.lightstreamer.client.ItemUpdate;

import java.util.ArrayList;

/**
 * Created by: allen on 16/8/29.
 */

public class MainSubscription extends SimpleSubscriptionListener {
    private static final String TAG = "MainSubscription";
    private ArrayList<StockForList> mLists;
    private Context mContext = new Context();

    public MainSubscription(ArrayList<StockForList> list) {
        super(TAG);
        this.mLists = list;
    }

    public void changeContext(Handler handler, ListView listView) {
        this.mContext.mHandler = handler;
        this.mContext.mListView = listView;
    }

    @Override
    public void onItemUpdate(ItemUpdate itemUpdate) {
        super.onItemUpdate(itemUpdate);
        final StockForList toUpdate = mLists.get(itemUpdate.getItemPos() - 1);
        toUpdate.update(itemUpdate, this.mContext);


    }

    public class Context {
        public Handler mHandler;
        public ListView mListView;
    }

}
