package com.yuan.dynamic.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yuan.dynamic.R;
import com.yuan.dynamic.net.DownloadTask;
import com.yuan.dynamic.utils.FileUtil;

import java.util.Objects;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

public class FlutterContainerActivity extends AppCompatActivity {

    private FlutterView mFlutterView;

    private ProgressDialog mProgressDialog;

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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("LOADING....");
        mProgressDialog.setMax(100);

        DownloadTask downloadTask = new DownloadTask(new DownloadTask.DownloadListener() {
            @Override
            public void onStart() {
                if (mProgressDialog != null) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {
                if (mProgressDialog != null) {
                    mProgressDialog.setProgress(progress);
                    if (progress == 100) {
                        mProgressDialog.setMessage("UNZIPING...");
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    new AlertDialog.Builder(FlutterContainerActivity.this).setTitle("提示").setMessage("下载或解压失败，请重试！").setPositiveButton("确定",
                            (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            }).show();

                }
            }

            @Override
            public void onSuccess() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(FlutterContainerActivity.this, "Load Success", Toast.LENGTH_SHORT).show();
                    setFlutterContentView();
                }
            }
        });
//
        String url = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        downloadTask.execute(url, FileUtil.getAarPath(this));

        setContentView(R.layout.activity_first);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String fileName = extras.getInt("type", 0) == 0 ? "libapp1.so" : "libapp2.so";
//
//            boolean needReplace = FileUtil.isNeedReplace(this, fileName);
//            if (needReplace) {
//                FileUtil.replaceSoFile(this, fileName);
//            }
//            FlutterView mFlutterView = Flutter.createView(this, getLifecycle(), "Android");
//            setContentView(mFlutterView);
//        } else {
//            Toast.makeText(this, "start failure!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

    private void setFlutterContentView() {
        mFlutterView = Flutter.createView(this, getLifecycle(), "Android");
        setContentView(mFlutterView);
    }

    @Override
    public void onBackPressed() {
        if (mFlutterView != null) {
            mFlutterView.popRoute();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

}
