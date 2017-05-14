package com.glowingsoft.uosbusstracking.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

/**
 * Created by khubab on 4/23/2017.
 */
public class Global {
    public static String IP="http://192.168.10.96";
    public static String base_url="/api/uos_managment.php";
    public static String KEYNAME="mySharedPreference";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String KEY_LOGIN="KEY_LOGIN";
    public static Context mContext;
    public CoordinatorLayout coordinatorLayout;
    public static String map_url="http://maps.google.com/maps?q=";
    // constructor
    public Global(Context context){
        this.mContext = context;
    }
   public static boolean isOnline()
    {
        ConnectivityManager conMgr =  (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            return false;
        }else{
            return true;
        }
    }
    public void showSnack(String message, CoordinatorLayout mycoordinateLayout)
    {
        coordinatorLayout=mycoordinateLayout;
        Snackbar snackbar=Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    public String getDeviceId()
    {
        return Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }

}
