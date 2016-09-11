package com.zklogic.androidutility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;



/**
 * Created by zk on 9/4/2015.
 */
public class ZKDialog {
    
    private static Dialog dialog;

    public static void showAlertDialog(Activity activity, View view){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(view);
        hideDialog();
        dialog = alertDialog.create();
        dialog.show();
    }

    public static  void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener okListener, String okLabel,
                                       DialogInterface.OnClickListener cancelListener, String cancelLabel) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if (title != null) {
            alertDialog.setTitle(title);
        }
        if (message != null) {
            alertDialog.setMessage(message);
        }


        if (okListener == null && okLabel != null) {
            alertDialog.setPositiveButton(okLabel, new DialogInterface.OnClickListener() {
                public  void onClick(DialogInterface dialog, int which) {
                    // continue
                    hideDialog();
                }
            });
        } else if (okListener != null) {
            alertDialog.setPositiveButton(okLabel, okListener);
        }

        if (cancelListener == null && cancelLabel != null) {
            alertDialog.setNegativeButton(cancelLabel, new DialogInterface.OnClickListener() {
                public  void onClick(DialogInterface dialog, int which) {
                    hideDialog();
                }
            });
        }
        else if(cancelListener !=null){
            alertDialog.setNegativeButton(cancelLabel,cancelListener);
        }
        hideDialog();
        dialog = alertDialog.create();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  static void hideDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
    }

    public static void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, null, "Ok", null, null);
    }

    public static void showAlertDialog(Context context, String message) {
        showAlertDialog(context, null, message, null, "Ok", null, null);
    }

    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener, String okLabel) {
        showAlertDialog(context, title, message, okListener, okLabel, null, null);
    }

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener okListener, String okLabel) {
        showAlertDialog(context, null, message, okListener, okLabel, null, null);
    }

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener okListener, String okLabel, DialogInterface.OnClickListener cancelListener, String cancelLabel) {
        showAlertDialog(context, null, message, okListener, okLabel, cancelListener, cancelLabel);
    }
}

