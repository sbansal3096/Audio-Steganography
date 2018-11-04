package com.example.shubham.SecureChat;

import android.content.SharedPreferences;
import android.content.Context;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";

    public static final String MESSAGE = "message";
    public static final String KEY = "key";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void storeMessage (String message) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MESSAGE, message);
        editor.apply();
    }

    public String getMessage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(MESSAGE, null);
    }

    public void storeKey (int key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY, key);
        editor.apply();
    }

    public int getKey() {
        return mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(KEY, -1);
    }

}
