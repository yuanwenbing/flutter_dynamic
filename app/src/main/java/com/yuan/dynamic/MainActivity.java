package com.yuan.dynamic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.replaceResFile(this, "res.apk");
    }

    public void onClick(View view) {
        if (view.getId() == R.id.first)
            FirstActivity.open(this, 0);
        else if (view.getId() == R.id.second)
            FirstActivity.open(this, 1);
        else if (view.getId() == R.id.third)
            ThirdActivity.open(this);
        else if (view.getId() == R.id.four)
            FourActivity.open(this);
    }
}
