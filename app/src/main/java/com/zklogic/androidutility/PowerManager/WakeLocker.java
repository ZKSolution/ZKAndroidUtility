package com.zklogic.androidutility.PowerManager;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by ArfanMirza on 3/24/2016.
 */
public class WakeLocker {

    private static PowerManager.WakeLock wakeLock;

    private static String packageName = "com.zklogic";

    public static void acquire(Context context,String appPackage){
        packageName = appPackage;
        acquire(context);
    }
    public static void acquire(Context context) {
        if (wakeLock != null)
            wakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        /*
		 * wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
		 * PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
		 * "Package Id");
		 */
        wakeLock = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE |
                        PowerManager.SCREEN_DIM_WAKE_LOCK, packageName);
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null)
            wakeLock.release();
        wakeLock = null;
    }
}
