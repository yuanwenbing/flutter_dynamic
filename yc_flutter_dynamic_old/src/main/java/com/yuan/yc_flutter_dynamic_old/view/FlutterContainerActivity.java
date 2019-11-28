package com.yuan.yc_flutter_dynamic_old.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yuan.yc_flutter_dynamic_old.net.FlutterManagerTask;
import com.yuan.yc_flutter_dynamic_old.utils.FileUtil;

import java.util.Objects;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;


/**
 * Created by yuan on 2019-11-19.
 * Email:yuanwb@yiche.com
 * <p>
 * Flutter 容器
 */
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
        mProgressDialog.setMessage("下载中....");
        mProgressDialog.setMax(100);

        FlutterManagerTask downloadTask = new FlutterManagerTask(new FlutterManagerTask.DownloadListener() {
            @Override
            public void onStart() {
                if (mProgressDialog != null) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {
                if (mProgressDialog != null) {
                    mProgressDialog.show();
                    mProgressDialog.setProgress(progress);
                    if (progress == 100) {
                        mProgressDialog.setMessage("解压中...");
                    } else if (progress == 101) {
                        mProgressDialog.setMessage("校验中...");
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
        String url = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        downloadTask.execute(url, FileUtil.getAarPath(this));
        setContentView(new View(this));
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
