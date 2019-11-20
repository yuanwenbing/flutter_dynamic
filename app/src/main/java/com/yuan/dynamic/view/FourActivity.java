package com.yuan.dynamic.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.yuan.dynamic.utils.PluginManager;

import io.flutter.app.FlutterFragmentActivity;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterView;

public class FourActivity extends FlutterFragmentActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FourActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean needReplace = PluginManager.isNeedReplace(this, "libapp2.so");
        if (needReplace) {
            PluginManager.replaceSoFile(this, "libapp2.so");
        }

        super.onCreate(savedInstanceState);
        setContentView(getFlutterView());
    }

    @Override
    public FlutterView createFlutterView(Context context) {
        WindowManager.LayoutParams matchParent = new WindowManager.LayoutParams(-1, -1);
        FlutterNativeView nativeView = this.createFlutterNativeView();
        FlutterView flutterView = new FlutterView(this, (AttributeSet) null, nativeView);
        flutterView.setLayoutParams(matchParent);
        return flutterView;
    }
}
