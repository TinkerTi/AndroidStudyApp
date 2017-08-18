package com.github.tinkerti.androidstudyapp;

import android.app.Application;

import com.github.tinkerti.androidstudyapp.crash.CrashHandler;

/**
 * Created by tiankui on 8/14/17.
 */

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
