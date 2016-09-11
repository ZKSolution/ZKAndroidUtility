package com.zklogic.androidutility.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by ArfanMirza on 12/4/2015.
 */
public class ConnectionStrength {

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = ConnectionStrength.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnected(Context context){
        NetworkInfo info = ConnectionStrength.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = ConnectionStrength.getNetworkInfo(context);
        return (info != null && info.isConnected() && ConnectionStrength.isConnectionFast(info.getType(),info.getSubtype()));
    }
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = ConnectionStrength.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type MOBILE 1xrtt 50-100kbps return false");
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type MOBILE CDMA 14-64kbps return false");
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type EDGE 50-100kbps return false");
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type EVDO_0 400-1000kbps return true");
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type EVDO_A 600-1400kbps return true");
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type GPRS 600-1400kbps return false");
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type HSDPA 2-4mbps return true");
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type HSPA 700-1700kbps return true");
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type HSUPA 1-23Mbps return true");
                    return true; // ~ 1-23 Mbps

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type UMTS 400-700kbps return true");
                    return true; // ~ 400-7000 kbps

                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type EHPRD 1-2mbps return true");
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type EVDO_B 5mbps return true");
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type HSPAP 10-20mbps return true");
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type IDEN 25kbps return false");
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type LTE 10mbps return true");
                    return true; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    Log.d("connection", "isOktosend checking isConnectionFast type of network type Unknown return false");
                default:
                    return false;
            }
        }else{
            return false;
        }
    }
}
