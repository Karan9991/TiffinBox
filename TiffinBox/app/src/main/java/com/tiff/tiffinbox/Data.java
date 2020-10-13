package com.tiff.tiffinbox;

import android.content.Context;
import android.net.ConnectivityManager;

public class Data {

    private static Data data = null;

    private Data(){

    }

    public static Data getInstance(){
        if (data == null){
            data = new Data();
        }
        return data;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
