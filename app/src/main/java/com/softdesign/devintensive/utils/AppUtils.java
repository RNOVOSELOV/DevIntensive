package com.softdesign.devintensive.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.softdesign.devintensive.data.managers.DataManager;
import com.squareup.picasso.Picasso;

/**
 * Created by roman on 13.07.16.
 */
public class AppUtils {

    public static String getPathByUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = DataManager.getInstance().getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
