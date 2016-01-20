package com.marshallepie.root.whatdidusay.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dottechnologies on 7/9/15.
 * This class is to check whether internet is on or off
 */
public class CheckInternet {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

}
