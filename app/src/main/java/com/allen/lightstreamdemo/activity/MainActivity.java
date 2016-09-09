package com.allen.lightstreamdemo.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.lightstreamdemo.fragment.DetailsFragment;
import com.allen.lightstreamdemo.LightStreamApplication;
import com.allen.lightstreamdemo.ILightStreamerClientProxy;
import com.allen.lightstreamdemo.R;
import com.allen.lightstreamdemo.fragment.StocksFragment;
import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.LightstreamerClient;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity implements StocksFragment.onStockSelectedListener {


    private static final String TAG = "MainActivity";
    private ClientListener currentListener = new LSClientListener();
    private boolean pnEnable = false;
    private boolean isConnectionExpected = false;
    private GestureDetectorCompat mDetectorCompat;
    ILightStreamerClientProxy mClientProxy;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientProxy = LightStreamApplication.sClientProxy;
        GestureControls gs = new GestureControls();
        mDetectorCompat = new GestureDetectorCompat(this, gs);
        mDetectorCompat.setOnDoubleTapListener(gs);
        this.mHandler = new Handler();
        getSupportActionBar().setTitle("外汇 Demo");
        setContentView(R.layout.stocks);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;

            }
            StocksFragment firstFragment = new StocksFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public void onStockSelected(int item) {
        Logger.d("Stock detail selected");
        DetailsFragment detailsFragment = getDetailsFragment();
        if (detailsFragment != null) {
            detailsFragment.updateStocksView(item);
        } else {
            DetailsFragment newFragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putInt(DetailsFragment.ARG_ITEM, item);
            args.putBoolean(DetailsFragment.ARG_PN_CONTROLS, pnEnable);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
            transaction.replace(R.id.fragment_container, newFragment, "DETAILS_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public class LSClientListener implements ClientListener {

        @Override
        public void onListenEnd(LightstreamerClient lightstreamerClient) {
            Logger.d(lightstreamerClient);
        }

        @Override
        public void onListenStart(LightstreamerClient lightstreamerClient) {
            this.onStatusChange(lightstreamerClient.getStatus());
            Logger.d("onListenStart: ");
        }

        @Override
        public void onServerError(int i, String s) {
            Logger.e( "Error" + i + " :" + s);
        }

        @Override
        public void onStatusChange(String status) {
            Logger.d("onStatusChange: "+status);
            mHandler.post(new StatusChange(status));
        }

        @Override
        public void onPropertyChange(String s) {
            Logger.d("onPropertyChange: "+s);

        }
    }

    private class StatusChange implements Runnable {
        private String status;

        public StatusChange(String status) {
            this.status = status;
        }

        private void applyStatus(int statusId, int textId) {
            ImageView statusIcon = (ImageView) findViewById(R.id.status_image);
            TextView textStatus = (TextView) findViewById(R.id.text_status);
            statusIcon.setContentDescription(getString(textId));
            statusIcon.setImageResource(statusId);
            textStatus.setText(getString(textId));
        }

        @Override
        public void run() {
            Logger.d( "run: "+status);
            switch (status) {

                case "CONNECTING":
                    applyStatus(R.mipmap.status_disconnected, R.string.status_connecting);
                    break;

                case "CONNECTED:STREAM-SENSING":
                    applyStatus(R.mipmap.status_connected_polling, R.string.status_connecting);
                    break;

                case "DISCONNECTED":
                    applyStatus(R.mipmap.status_disconnected, R.string.status_disconnected);
                    break;
                case "DISCONNECTED:WILL-RETRY":
                    applyStatus(R.mipmap.status_disconnected, R.string.status_waiting);
                    break;

                case "CONNECTED:HTTP-STREAMING":
                    applyStatus(R.mipmap.status_connected_streaming, R.string.status_streaming);
                    break;
                case "CONNECTED:WS-STREAMING":
                    applyStatus(R.mipmap.status_connected_streaming, R.string.status_ws_streaming);
                    break;

                case "CONNECTED:HTTP-POLLING":
                    applyStatus(R.mipmap.status_connected_polling, R.string.status_polling);
                    break;
                case "CONNECTED:WS-POLLING":
                    applyStatus(R.mipmap.status_connected_polling, R.string.status_ws_polling);
                    break;

                case "STALLED":
                    applyStatus(R.mipmap.status_stalled, R.string.status_stalled);
                    break;

                default:
                    Log.wtf(TAG, "Recevied unexpected connection status: " + status);
                    return;
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private int getIntentItem() {
        int openItem = 0;
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            Bundle extras = launchIntent.getExtras();
            if (extras != null) {
                openItem = extras.getInt("itemNum");
            }
        }
        return openItem;
    }

    @Override
    public void onNewIntent(Intent intent) {
        Logger.d("New intent received");
        setIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mClientProxy.removeListener(currentListener);
        mClientProxy.stop(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mClientProxy.addListener(this.currentListener);
        isConnectionExpected = mClientProxy.start(false);

        int openItem = getIntentItem();
        if (openItem == 0 && findViewById(R.id.fragment_container) == null) {
            //tablet, always start with an open stock
            DetailsFragment df = getDetailsFragment();
            if (df != null) {
                openItem = df.getCurrentStock();
            }

            if (openItem == 0) {
                openItem = 2;
            }
        }

        if (openItem != 0) {
            onStockSelected(openItem);
        }
    }

    private DetailsFragment getDetailsFragment() {
        DetailsFragment detailsFrag = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.details_fragment);


        if (detailsFrag == null) {
            //phones
            detailsFrag = (DetailsFragment) getFragmentManager().findFragmentByTag("DETAILS_FRAGMENT");
        } // else tablets

        return detailsFrag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.v(TAG, "Switch button: " + isConnectionExpected);
        menu.findItem(R.id.start).setVisible(!isConnectionExpected);
        menu.findItem(R.id.stop).setVisible(isConnectionExpected);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.stop) {
            Log.i(TAG, "Stop");
            supportInvalidateOptionsMenu();
            mClientProxy.stop(true);
            isConnectionExpected = false;
            return true;
        } else if (itemId == R.id.start) {
            Log.i(TAG, "Start");
            supportInvalidateOptionsMenu();
            mClientProxy.start(true);
            isConnectionExpected = true;
            return true;
        } else if (itemId == R.id.about) {
            new AboutDialog().show(getFragmentManager(), null);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static class AboutDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_about, null)).setPositiveButton("OK", null);
            return builder.create();
        }
    }

    private class GestureControls extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            TextView textStatus = (TextView) findViewById(R.id.text_status);
            textStatus.setVisibility(textStatus.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            return true;
        }
    }
}
