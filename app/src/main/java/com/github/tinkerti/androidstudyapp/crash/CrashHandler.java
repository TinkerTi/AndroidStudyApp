package com.github.tinkerti.androidstudyapp.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tiankui on 8/14/17.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sInstance;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context context;

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    private CrashHandler() {
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSdCard(e);
        } catch (Exception exception) {

        }
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSdCard(Throwable throwable) {
        File path = context.getCacheDir();
        String storePath = path.getAbsolutePath() + "crashTest/log";
        File file = new File(storePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File crashFile = new File(file + "crash" + time + ".trace");
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(crashFile)));
            printWriter.write(time);
            dumpPhoneInfo(printWriter);
            throwable.printStackTrace(printWriter);
            printWriter.close();
        } catch (Exception e) {

        }
    }

    private void dumpPhoneInfo(PrintWriter printWriter) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            printWriter.print("App version:");
            printWriter.print(packageInfo.versionName);
            printWriter.print("_");
            printWriter.println(packageInfo.versionCode);

            printWriter.print("OS version:");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print("_");
            printWriter.println(Build.VERSION.SDK_INT);

            printWriter.print("vendor:");
            printWriter.println(Build.MANUFACTURER);

            printWriter.print("Model:");
            printWriter.println(Build.MODEL);

            printWriter.print("CPU ABI:");
            printWriter.println(Build.CPU_ABI);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
