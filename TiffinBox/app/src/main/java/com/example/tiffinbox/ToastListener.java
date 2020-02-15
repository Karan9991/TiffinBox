package com.example.tiffinbox;

import android.content.Context;
import android.widget.Toast;

public interface ToastListener {
    public static void shortToast(Context ctx, String msg){
        Toast.makeText(ctx,msg, Toast.LENGTH_SHORT).show();
    }
    public static void longToast(Context ctx, String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }
}
