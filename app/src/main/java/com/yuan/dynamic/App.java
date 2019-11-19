package com.yuan.dynamic;

import android.app.Application;

import io.flutter.view.FlutterMain;

/**
 * Created by yuan on 2019-11-19.
 * Email:yuanwb@yiche.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlutterMain.startInitialization(getApplicationContext());
        FlutterMain.ensureInitializationComplete(getApplicationContext(), (String[]) null);
    }
}
