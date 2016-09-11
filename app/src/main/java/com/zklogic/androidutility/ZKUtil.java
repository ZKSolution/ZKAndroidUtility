package com.zklogic.androidutility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by ArfanMirza on 8/24/2015.
 */
public class ZKUtil {

    private static final String TAG = "ZKUtil";
    public static final boolean isDebug = true;
    public static final boolean isLive = false;

    public static final String BROADCAST_DATA_KEY = "ZK_b_key_data";

    public static void setAlarm(Calendar calendar, Context context, Intent alarmIntent, int alarmUniqueCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        PendingIntent pi = PendingIntent.getBroadcast(context, alarmUniqueCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            showLog("setExact");
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            showLog("alarmManager.set");
        }
    }

    public static boolean isRunningOnEmulator() {
        boolean result = //
                Build.FINGERPRINT.startsWith("generic")//
                        || Build.FINGERPRINT.startsWith("unknown")//
                        || Build.MODEL.contains("google_sdk")//
                        || Build.MODEL.contains("Emulator")//
                        || Build.MODEL.contains("Android SDK built for x86")
                        || Build.MANUFACTURER.contains("Genymotion");
        if (result)
            return true;
        result |= Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
        if (result)
            return true;
        result |= "google_sdk".equals(Build.PRODUCT);
        return result;
    }

    public static void showLog(String msg) {
        if (isDebuggeVersion()) {
            Log.i(TAG, msg);
        }
    }

    public static void showLog(Throwable throwable) {
        if (isDebuggeVersion()) {
            throwable.printStackTrace();
        }
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int toastDuration) {
        Toast.makeText(context, text, toastDuration).show();
    }


    public static void showLog(Exception exception) {
        showLog((Throwable) exception);
    }

    public static void showLog(Error error) {
        showLog((Throwable) error);
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static Bitmap convertImageViewToBitmap(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    public static InputStream convertBitmapToInputPNGStream(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return convertBitmapToInputPNGStream(drawable.getBitmap());
    }

    public static InputStream convertBitmapToInputJPGStream(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return convertBitmapToInputJPGStream(drawable.getBitmap());
    }

    public static InputStream convertBitmapToInputJPGStream(ImageView imageView, int opacity) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return convertBitmapToInputJPGStream(drawable.getBitmap(), opacity);
    }

    public static InputStream convertBitmapToInputPNGStream(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public static InputStream convertBitmapToInputJPGStream(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public static InputStream convertBitmapToInputJPGStream(Bitmap bitmap, int opacity) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, opacity, stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public static  InputStream convertFileToInputStream(File file){
        if(file.exists()){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                showLog(e);
            }
        }
        return  null;
    }

    public static  InputStream convertFilePathToInputStream(String filePath){
        File file = new File(filePath);
        return  convertFileToInputStream(file);
    }

    public static Long getUTCZERO() {

        int utcOffset = getUTCOffset();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, -utcOffset);
        long miliSec = c.getTimeInMillis();
        showLog("getUTCZERO: " + miliSec);

        return miliSec;
    }

    public static int getUTCOffset() {

        Calendar c = Calendar.getInstance();
        int utcOffset = (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)) / 1000;

        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;

        showLog("getfUTCOfset: " + utcOffset + "  " + offsetFromUtc);
        return offsetFromUtc;
    }


    public static void showLog(Object object) {
        showLog(object.toString());
    }

    public static void showLogWithTag(String msg, String tag) {
        Log.i(tag, msg);
    }

    public static void showLogWithTag(Object object, String tag) {
        showLogWithTag(object.toString(), tag);
    }

    public static boolean isDebuggeVersion() {
        return isDebug || BuildConfig.DEBUG;
    }

    public static int getRandomNumber(int min, int max) {
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return i1;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isAppAtTop(Context context, String packageid) {

        ActivityManager am = (ActivityManager) context.
                getSystemService(Activity.ACTIVITY_SERVICE);
        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        return packageid.equalsIgnoreCase(packageName);

    }
}

