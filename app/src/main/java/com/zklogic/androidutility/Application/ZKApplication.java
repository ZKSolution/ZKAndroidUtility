package com.zklogic.androidutility.Application;

import android.support.multidex.MultiDexApplication;

/**
 * Created by ArfanMirza on 12/1/2015.
 */
public class ZKApplication extends MultiDexApplication {
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}

