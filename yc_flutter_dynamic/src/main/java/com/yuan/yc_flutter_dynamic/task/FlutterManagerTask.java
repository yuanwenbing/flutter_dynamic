package com.yuan.yc_flutter_dynamic.task;

import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.util.Log;

import com.yuan.yc_flutter_dynamic.BuildConfig;
import com.yuan.yc_flutter_dynamic.task.journa.FJournal;
import com.yuan.yc_flutter_dynamic.utils.CloseIoUtils;
import com.yuan.yc_flutter_dynamic.utils.FileUtil;
import com.yuan.yc_flutter_dynamic.utils.ZipUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yuan on 2019-11-21.
 * Email:yuanwb@yiche.com
 */
public class FlutterManagerTask extends AsyncTask<String, Integer, Boolean> {

    private DownloadListener mListener;

    public FlutterManagerTask(DownloadListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {


        long start = System.currentTimeMillis();

        if (strings == null || strings.length != 2) {
            Log.d("FlutterManagerTask", "arguments error");
            return false;
        }

        String downloadUrl = strings[0];
        String parentPath = strings[1];
        URL url;
        HttpURLConnection connection;

        FileOutputStream fos = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        String arm = "arm64-v8a";
        try {
            // http://172.20.30.83:8888/flutter_release-1.0.aar
            url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            String urlFile = connection.getURL().getFile(); //    /flutter_release-1.0.aar
            String fileName = urlFile.substring(urlFile.lastIndexOf(File.separatorChar) + 1); // 去掉斜杠 flutter_release-1.0.aar
            String subPath = fileName.substring(0, fileName.lastIndexOf(".")); // 去掉后缀 flutter_release-1.0 作为下载文件的目录
            File filePath = new File(parentPath + File.separator + subPath); // 拼成下载地址 例如 data/data/'packageName'/p/flutter_release-1.0
            File productFile = new File(filePath, fileName);
            File soFile = new File(filePath.getAbsolutePath() + File.separator + "jni" + File.separator + arm + File.separator + "libapp.so");

            FJournal fJournal = FJournal.open(filePath.getAbsolutePath());

            Log.d("FlutterManagerTask", fJournal.read() + "");
            if (!productFile.exists() || !FJournal.SUCCESS.equals(fJournal.read())) {
                if (!filePath.exists()) {
                    boolean mkdirs = filePath.mkdirs();
                    if (!mkdirs) {
                        Log.d("FlutterManagerTask", "mk flutter product failure");
                        return false;
                    }
                }
                fJournal.write(FJournal.DIRTY);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Charset", "utf-8");
                connection.connect();
                fos = new FileOutputStream(new File(filePath, fileName));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    is = connection.getInputStream();
                    int contentLength = connection.getContentLength();
                    bis = new BufferedInputStream(is);
                    int len;
                    int total = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = bis.read(bytes)) != -1) {
                        total += len;
                        fos.write(bytes, 0, len);
                        float progress = total * 100f / contentLength;
                        publishProgress((int) progress);
                    }
                }
                fJournal.write(FJournal.DOWNLOAD);
                ZipUtil.UnZipFolder(new File(filePath, fileName).getAbsolutePath(), filePath.getAbsolutePath());
                fJournal.write(FJournal.UNZIP);
                publishProgress(101);
                FileUtil.replaceAssetFile(productFile.getAbsolutePath(), parentPath);
                fJournal.write(FJournal.REPLACE_ASSETS);
                FileUtil.replaceSoFile(soFile.getAbsolutePath(), parentPath);
                fJournal.write(FJournal.REPLACE_SO);
                fJournal.write(FJournal.SUCCESS);
            } else {
                // 两者都存在，不用解压
                publishProgress(101);
                if (productFile.exists() && soFile.exists()) {

                    // 产物文件
                    File realProductFile = new File(new File(parentPath).getParentFile(), "res.apk");
                    if (realProductFile.exists()) {
                        if (!FileUtil.getFileMD5(realProductFile).equals(FileUtil.getFileMD5(productFile))) {
                            FileUtil.replaceAssetFile(productFile.getAbsolutePath(), parentPath);
                        } else {
                            Log.d("FlutterManagerTask", "not replace product");
                        }
                    } else {
                        FileUtil.replaceAssetFile(productFile.getAbsolutePath(), parentPath);
                    }

                    // so文件
                    File realSoFile = new File(new File(parentPath).getParentFile(), "libapp.so");
                    if (realSoFile.exists()) {
                        if (!FileUtil.getFileMD5(realSoFile).equals(FileUtil.getFileMD5(soFile))) {
                            FileUtil.replaceSoFile(soFile.getAbsolutePath(), parentPath);
                        } else {
                            Log.d("FlutterManagerTask", "not replace so");
                        }
                    } else {
                        FileUtil.replaceSoFile(soFile.getAbsolutePath(), parentPath);
                    }
                } else {
                    boolean delete = productFile.delete();
                    if (!delete) {
                        if (BuildConfig.DEBUG) {
                            Log.d("FlutterManagerTask", "delete failure!");
                        }
                    }
                    return false;
                }
            }

            long end = System.currentTimeMillis();

            if (BuildConfig.DEBUG) {
                Log.d("FlutterManagerTask", "end- start:" + (end - start));
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseIoUtils.closeIO(fos, is, bis);
        }

    }

    @Override
    @UiThread
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mListener != null) {
            mListener.onProgress(values[0]);
        }
    }

    @Override
    @UiThread
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        if (mListener != null) {
            if (bool) {
                mListener.onSuccess();
            } else {
                mListener.onFailure(new Exception("download failure!"));
            }
        }

    }


    public interface DownloadListener {
        @UiThread
        void onStart();

        @UiThread
        void onProgress(int progress);

        @UiThread
        void onFailure(Exception e);

        @UiThread
        void onSuccess();

    }
}
