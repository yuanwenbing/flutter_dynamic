package com.yiche.dynamic;

import android.app.Application;

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
        String processName = getProcessName();
        String packageName = getPackageName();

        // 主进程初始化
        if (packageName.equals(processName)) {
            // init something
        }
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
