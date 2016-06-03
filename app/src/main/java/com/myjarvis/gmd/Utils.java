package com.myjarvis.gmd;
/*this project is the property of jarvis
   Sachin*/
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by jarvis on 26-May-16
 * at  11:45 AM .
 */
public class Utils {




    public static boolean checkPermission(Context context, String callPhone) {
        int result = ContextCompat.checkSelfPermission(context, callPhone);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
