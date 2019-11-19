package com.yuan.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

public class SecondActivity extends AppCompatActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SecondActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        boolean needReplace = PluginManager.isNeedReplace(this, "libapp2.so");
        if (needReplace) {
            PluginManager.replaceSoFile(this, "libapp2.so");
//            Utils.restartApp(this);
//        }else{
            FlutterView flutterView = Flutter.createView(this, getLifecycle(), "Android");
            setContentView(flutterView);
        }
    }
}
