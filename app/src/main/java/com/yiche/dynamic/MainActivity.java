package com.yiche.dynamic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuan.yc_flutter_dynamic_old.view.FlutterContainerActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.first) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 0);
            bundle.putString("url", getHost() + "/flutter_release-1.0.aar");
            FlutterContainerActivity.open(this, bundle);
        } else if (view.getId() == R.id.second) {
            Bundle bundle = new Bundle();
            bundle.putString("url", getHost() + "/flutter_release-1.1.aar");
            bundle.putInt("type", 1);
            FlutterContainerActivity.open(this, bundle);
        } else if (view.getId() == R.id.third) {
            Bundle bundle = new Bundle();
            bundle.putString("url", getHost() + "/flutter_release-1.2.aar");
            bundle.putInt("type", 1);
            FlutterContainerActivity.open(this, bundle);
        } else if (view.getId() == R.id.fourth) {
        } else if (view.getId() == R.id.fifth) {
            Bundle bundle = new Bundle();
            bundle.putString("url", getHost() + "/flutter_lottery-1.0.aar");
            bundle.putInt("type", 1);
            FlutterContainerActivity.open(this, bundle);
        }
    }

    @NonNull
    private String getHost() {
        return "http://172.20.30.83:8888";
    }
}
