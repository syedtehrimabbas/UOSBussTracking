package com.glowingsoft.uosbusstracking;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

/**
 * Created by khubab on 5/12/2017.
 */
public class MyApplication extends Application {
public static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
    public void navigate(Context from,Class to) {
        startActivity(new Intent(from, to));
    }
    public static  MyApplication getInstance()
    {
        return instance ;
    }
}
