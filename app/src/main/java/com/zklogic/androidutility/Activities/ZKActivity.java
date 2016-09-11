package com.zklogic.androidutility.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.zklogic.androidutility.Fragments.ZKFragments;
import com.zklogic.androidutility.Network.ConnectionStrength;
import com.zklogic.androidutility.R;
import com.zklogic.androidutility.ZKUtil;

/**
 * Created by ArfanMirza on 12/4/2015.
 */
public class ZKActivity extends FragmentActivity {

    private ZKFragments activeFragmet;


    private static ProgressDialog progress;

    private boolean isNetworkActivityMoniter = false;

    public void setNetworkActivityMoniter(boolean isNetworkActivityMoniter) {
        this.isNetworkActivityMoniter = isNetworkActivityMoniter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkActivityMoniter)
        {
            activeNetwrokStateReceiver();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    protected void showNetworkDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //alert.setCancelable(false);
        alert.setTitle(getString(R.string.NetworkAlert));
        alert.setMessage(getString(R.string.No_Internet_connection_detected));
        alert.setNegativeButton("okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        }).show();
    }

    BroadcastReceiver networkStateReceiver;

    public void netWorkActivityOccur(boolean isConnected) {
        ZKUtil.showLog("netWorkActivityOccur: " + isConnected);
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
        try {
            if (networkStateReceiver != null) {
                unregisterReceiver(networkStateReceiver);
            }
        } catch (Exception e) {
            ZKUtil.showLog(e);
        }
    }



    protected void replaceFragment(ZKFragments frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        activeFragmet = frag;
        ft.replace(R.id.fragment_house, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    protected void replaceAnimFragment(ZKFragments frag, int left, int right) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            activeFragmet = frag;
            ft.setCustomAnimations(left, right);
            ft.replace(R.id.fragment_house, frag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        } catch (Throwable e) {
            ZKUtil.showLog(e);
        }
    }

    protected void hideKeyBoard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void hideProgressDialog() {
//        if (true) {
//            return;
//        }
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

    protected void showLog(String msg) {
        ZKUtil.showLog(msg);
    }
}
