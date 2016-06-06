package c.proyecto.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public final class UtilMethods {

    public static final int TAG_WRITE_STORAGE_PERMISION = 0;
    public static final int TAG_LOCATION_PERMISION = 1;

    public static  boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("WriteStorage", "Permission is granted");
                return true;
            } else {

                Log.v("WriteStorage","Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAG_WRITE_STORAGE_PERMISION);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("WriteStorage","Permission is granted");
            return true;
        }

    }

    public static boolean isUbicationPermissionGranted(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Ubication", "Permission is granted");
                return true;
            } else {

                Log.v("Ubication","Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, TAG_LOCATION_PERMISION);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Ubication","Permission is granted");
            return true;
        }
    }
}
