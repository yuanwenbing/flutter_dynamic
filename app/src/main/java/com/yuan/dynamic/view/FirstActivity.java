package com.yuan.dynamic.view;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuan.dynamic.utils.PluginManager;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

public class FirstActivity extends AppCompatActivity {


    public static void open(Context context, int type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.setClass(context, FirstActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        String fileName =  getIntent().getIntExtra("type", 0) == 0 ? "libapp1.so" : "libapp2.so";

        boolean needReplace = PluginManager.isNeedReplace(this, fileName);
        if (needReplace) {
            PluginManager.replaceSoFile(this, fileName);
        }
        FlutterView flutterView = Flutter.createView(this, getLifecycle(), "Android");
        setContentView(flutterView);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
}
