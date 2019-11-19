package com.yuan.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.WindowManager;

import io.flutter.app.FlutterFragmentActivity;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterView;

public class ThirdActivity extends FlutterFragmentActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ThirdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        boolean needReplace = PluginManager.isNeedReplace(this, "libapp1.so");
        if (needReplace) {
            PluginManager.replaceSoFile(this, "libapp1.so");
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
