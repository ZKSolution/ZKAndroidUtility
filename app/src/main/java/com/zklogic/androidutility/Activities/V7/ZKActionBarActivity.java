package com.zklogic.androidutility.Activities.V7;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
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
public class ZKActionBarActivity extends ActionBarActivity {
    private static ProgressDialog progress;
    protected Handler handler = new Handler();
    protected Runnable networkWatcher = new Runnable() {
        @Override
        public void run() {
            networknBreakDown();
        }
    };
    BroadcastReceiver networkStateReceiver;
    BroadcastReceiver rootReceiver;
    private boolean isDebug = false;
    private ArrayList<String> broadCastList;
    private boolean isResumeFirstTime = false;
    private boolean isNetworkActivityMoniter = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadCastList = new ArrayList();
        rootReceiver = new RootReceiver();
        isResumeFirstTime = false;
        setDebug(true);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            showLog("Called from history");
        } else {
            showLog("Called from home");
        }
        setDebug(false);

    }

    public void sendLocalBroadCast(String broadCast) {
        showLog("sendLocalBroadCast: " + broadCast);
        Intent intent = new Intent(broadCast);
        intent.setAction(broadCast);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void sendLocalBroadCast(String broadCast, Bundle data) {
        Intent intent = new Intent(broadCast);
        intent.putExtra(ZKUtil.BROADCAST_DATA_KEY, data);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    protected void addBroadCast(String broadCastAction) {
        broadCastList.add(broadCastAction);
    }

    protected void removeBroadCast(String broadCastAction) {
        broadCastList.remove(broadCastAction);
    }

    public void setNetworkActivityMoniter(boolean isNetworkActivityMoniter) {
        this.isNetworkActivityMoniter = isNetworkActivityMoniter;
    }

    protected void trackLog(String log) {
        ZKTracker.getInstanace().trackExLog("AM: " + log);
    }

    protected void trackException(Throwable exception) {
        ZKTracker.getInstanace().trackExLog(exception);
    }

    protected void startNetworkWatcher() {
        showLog("startNetworkWatcher");
        handler.postDelayed(networkWatcher, 120 * 1000);
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

    public boolean isResumeFirstTime() {
        return isResumeFirstTime;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activeRootBroadCast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceivers();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isResumeFirstTime == false) {
            isResumeFirstTime = true;
        }
        hideKeyBoard();
        if (ZKUtil.isLive) {
            sendTrackRecord();
        }
        if (isNetworkActivityMoniter) {
            activeNetwrokStateReceiver();
        }


    }


//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
//            // Get called every-time when application went to background.
//            System.exit(0);
//        }
//
//
//    }

    protected void showNetworkDialog() {
//        Builder alert = new Builder(this);
//        alert.setCancelable(false);
////        alert.setTitle(getString(R.string.NetworkAlert));
////        alert.setMessage(getString(R.string.No_Internet_connection_detected));
//        alert.setTitle(getString(R.string.app_name));
//        alert.setMessage("No_Internet_connection_detected");
//        alert.setNegativeButton("okay", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                arg0.cancel();
//            }
//        }).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ZKDialog.showAlertDialog(ZKActionBarActivity.this, "Network", "No Network");
            }
        });

    }

    @Override
    protected void onPause() {
        // TODO Generated method by Arfan Mirza
        super.onPause();
        hideKeyBoard();

//        if (ZKUtil.isAppAtTop(getApplicationContext(), Utils.PACKAGE_ID) == false) {
//            ZKUtil.showLog("LEAVE");
//            ZKUtil.leave(getApplicationContext());
//        }

    }

    protected void hideKeyBoard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void sendTrackRecord() {
//        if (Build.VERSION.SDK_INT >= 9) {
//            Tracker t = ((BootyshakeApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
//            String name = getClass().getName();
//            name = name.substring(name.lastIndexOf('.') + 1);
//            t.setScreenName(name);
//            t.send(new HitBuilders.AppViewBuilder().build());
//        }
    }

    public void netWorkActivityOccur(boolean isConnected) {
        ZKUtil.showLog("netWorkActivityOccur: " + isConnected);
        if (isConnected == false) {
            showNetworkDialog();
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
                LocalBroadcastManager.getInstance(this).registerReceiver(rootReceiver, filter);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
    }

    private void activeNetwrokStateReceiver() {
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        ZKUtil.showLog("Mobitering Started For: " + name);
        networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Generated method by Arfan Mirza
                netWorkActivityOccur(ConnectionStrength.isConnected(context));
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
    }

    private void unregisterReceivers() {
        showLog("unregisterReceivers");
        try {
            if (networkStateReceiver != null) {
                unregisterReceiver(networkStateReceiver);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
        try {

            //unregisterReceiver(rootReceiver);
            int size = broadCastList.size();
            if (size > 0) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(rootReceiver);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
    }

    protected void showLog(String msg) {
        if (isDebug) {
            ZKUtil.showLog(msg);
        }
    }


    protected void hideProgressDialog() {
//        if (true) {
//            return;
//        }
        runOnUiThread(new Runnable() {
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
        showProgressDialogWithBackAction(this, false, null);
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
            ZKActionBarActivity.this.onReceive(context, intent);
        }
    }
}
