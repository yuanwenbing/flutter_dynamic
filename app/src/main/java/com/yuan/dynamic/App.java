package com.yuan.dynamic;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by yuan on 2019-11-19.
 * Email:yuanwb@yiche.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
//        Log.d("Applications", "Process.myPid():" + Process.myPid());
//        String processName = getProcessName();
//        if (BuildConfig.DEBUG) Log.d("Applications", processName);
        super.onCreate();
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
