package com.zklogic.androidutility.ImageUtil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by ArfanMirza on 3/20/2016.
 */
public class ZKImageUtils {

    public static Uri getImageUriFromIntent(Intent data) {
        Uri selectedImage = data.getData();
        return selectedImage;
    }

    public static String getImagePathFromIntent(Context context, Intent data) {
        Uri selectedImage = getImageUriFromIntent(data);
        return getImagePathFromUri(context, selectedImage);
    }


    public static String getImagePathFromUri(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
