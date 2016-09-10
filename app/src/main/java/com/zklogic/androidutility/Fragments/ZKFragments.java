package com.zklogic.androidutility.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zklogic.androidutility.Network.ConnectionStrength;
import com.zklogic.androidutility.R;
import com.zklogic.androidutility.Tracker.ZKTracker;
import com.zklogic.androidutility.ZKDialog;
import com.zklogic.androidutility.ZKUtil;

import java.util.ArrayList;

/**
 * Created by ArfanMirza on 12/4/2015.
 */
public class ZKFragments extends Fragment {

    private static ProgressDialog progress;
    protected Handler handler = new Handler();
    protected Runnable networkWatcher = new Runnable() {
        @Override
        public void run() {
            networknBreakDown();
        }
    };
    BroadcastReceiver networkStateReceiver;
    private boolean isNetworkActivityMoniter = false;
    private boolean isDebug = false;
    private int WATCHER_TIME = 30;
    private RootReceiver rootReceiver = new RootReceiver();
    private ArrayList<String> broadCastList = new ArrayList();
    private String name = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         broadCastList = new ArrayList();
//        rootReceiver = new RootReceiver();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void addBroadCast(String broadCastAction) {
//        if(broadCastList == null){
//            broadCastList = new ArrayList<>();
//        }
        broadCastList.add(broadCastAction);
    }

    public void removeBroadCast(String broadCastAction) {
        if (broadCastList != null) {
            broadCastList.remove(broadCastAction);
        }
    }

    public void netWorkActivityOccur(boolean isConnected) {
        if (isConnected == false) {
            showNetworkDialog();
        }
    }

    private void activeNetwrokStateReceiver() {
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        ZKUtil.showLog("Monitering Started For: " + name);
        networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Generated method by Arfan Mirza
                netWorkActivityOccur(ConnectionStrength.isConnected(context));
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkStateReceiver, filter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Generated method by Arfan Mirza
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == false) {
            unregisterReceivers();
        } else {
            hideKeyBoard();
            if (isNetworkActivityMoniter) {
                activeNetwrokStateReceiver();
            }
            activeRootBroadCast();
        }
    }

    private void activeRootBroadCast() {
        try {
            int size = broadCastList.size();
            if (size > 0) {
                IntentFilter filter = new IntentFilter();
                for (int ak = 0; ak < size; ak++) {
                    filter.addAction(broadCastList.get(ak));
                }
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rootReceiver, filter);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        sendTrackRecord();

    }

    public void setNetworkActivityMoniter(boolean isNetworkActivityMoniter) {
        this.isNetworkActivityMoniter = isNetworkActivityMoniter;
    }

    @Override
    public void onPause() {
        // TODO Generated method by Arfan Mirza
        super.onPause();
        unregisterReceivers();
    }

    private void unregisterReceivers() {
        try {
            if (networkStateReceiver != null) {
                getActivity().unregisterReceiver(networkStateReceiver);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
        try {

            //unregisterReceiver(rootReceiver);
            int size = broadCastList.size();
            if (size > 0) {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rootReceiver);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    protected void showLog(String msg) {
        if (isDebug()) {
            ZKUtil.showLog(msg);
        }
    }

    protected void trackLog(String log) {
        ZKTracker.getInstanace().trackExLog(name + ": " + log);
    }

    protected void trackLog(Object object) {
        trackLog(object.toString());
    }

    protected void trackException(Throwable exception) {
        ZKTracker.getInstanace().trackExLog(name + ": " + exception);
    }

    protected void hideKeyBoard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void sendTrackRecord() {
//		Tracker t = ((BootyshakeApplication) ((RootActivity) getActivity()).getApplication()).getTracker(TrackerName.APP_TRACKER);
//		String name = getClass().getName();
//		name = name.substring(name.lastIndexOf('.') + 1);
//		t.setScreenName(name);
//		t.send(new HitBuilders.AppViewBuilder().build());
    }

    protected void showNetworkDialog() {
//		Builder alert = new Builder(getActivity());
//		alert.setCancelable(false);
//		alert.setTitle(getString(R.string.app_name));
//		alert.setMessage("No_Internet_connection_detected");
//		alert.setNegativeButton("okay", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface arg0, int arg1) {
//				arg0.cancel();
//			}
//		}).show();
        ZKDialog.showAlertDialog(getActivity(), "Network", "No Network");

    }

    public int getWATCHER_TIME() {
        return WATCHER_TIME;
    }

    public void setWATCHER_TIME(int watcher_time_seconds) {
        if (watcher_time_seconds > 0)
            this.WATCHER_TIME = watcher_time_seconds;
    }

    protected void startNetworkWatcher() {
        showLog("startNetworkWatcher");
        handler.postDelayed(networkWatcher, WATCHER_TIME * 1000);
    }


    public void networknBreakDown() {
        hideProgressDialog();
        hideKeyBoard();
        showNetworkDialog();
    }

    protected void stopNetworkWatcher() {
        handler.removeCallbacks(networkWatcher);

    }

    protected EditText.OnEditorActionListener addEnterListner(EditText editText) {
        EditText.OnEditorActionListener onEditorActionListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitEnterKey();
                    return true;
                }
                return false;
            }
        };
        editText.setOnEditorActionListener(onEditorActionListener);
        return onEditorActionListener;
    }

    protected void submitEnterKey() {
        hideKeyBoard();
    }

    protected void hideProgressDialog() {
//        if (true) {
//            return;
//        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (progress != null) {

                        if (progress != null && progress.isShowing()) {
                            progress.hide();
                        }
                        if (progress != null) {
                            progress.dismiss();
                        }

                    }
                    progress = null;

                } catch (Exception e) {
                    e.printStackTrace();
                    progress = null;
                }
            }
        });

    }

    protected void showProgressDialog() {

        hideProgressDialog();
        showProgressDialogWithBackAction(getActivity(), false, null);
    }

    protected void showProgressDialogWithBackAction(Context context, boolean isBack, DialogInterface.OnCancelListener listener) {
        // hideProgressDialog();
//        if (true) {
//            return;
//        }
        if (ZKUtil.isRunningOnEmulator()) {
            return;
        }
        progress = new ProgressDialog(context) {
            private ProgressBar bar;

            protected void onCreate(android.os.Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.zk_progress_view);
                bar = (ProgressBar) findViewById(R.id.bar);
                bar.setHapticFeedbackEnabled(true);
            }
        };
        if (listener != null) {
            progress.setOnCancelListener(listener);
        }
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(isBack);

        try {
            progress.show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            onReceive(context, intent, action);
        } else {
            ZKUtil.showLog("OnRecive without action!!!");
        }
    }

    public void onReceive(Context context, Intent intent, String action) {
        ZKUtil.showLog("OnRecive: " + action);
    }

    private class RootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ZKFragments.this.onReceive(context, intent);
        }
    }
}
