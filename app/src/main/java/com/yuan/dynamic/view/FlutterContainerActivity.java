package com.yuan.dynamic.view;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yuan.dynamic.utils.PluginManager;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

public class FlutterContainerActivity extends AppCompatActivity {

    public static void open(Context context, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(context, FlutterContainerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fileName = extras.getInt("type", 0) == 0 ? "libapp1.so" : "libapp2.so";

            boolean needReplace = PluginManager.isNeedReplace(this, fileName);
            if (needReplace) {
                PluginManager.replaceSoFile(this, fileName);
            }
            FlutterView flutterView = Flutter.createView(this, getLifecycle(), "Android");
            setContentView(flutterView);
        } else {
            Toast.makeText(this, "启动失败", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
}
