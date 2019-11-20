package com.yuan.dynamic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuan.dynamic.utils.PluginManager;
import com.yuan.dynamic.view.FlutterContainerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.replaceResFile(this, "res.apk");
    }

    public void onClick(View view) {
        if (view.getId() == R.id.first) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 0);
            FlutterContainerActivity.open(this, bundle);
        } else if (view.getId() == R.id.second) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            FlutterContainerActivity.open(this, bundle);
        }
    }
}
