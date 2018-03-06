package com.mdht.shopping.spping;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ${Sunanang} on 17/11/3.
 */

public class MyServices extends Service{


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Sunanang","service");
    }
}
