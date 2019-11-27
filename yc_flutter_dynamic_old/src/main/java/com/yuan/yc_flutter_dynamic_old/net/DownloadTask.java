package com.yuan.yc_flutter_dynamic_old.net;

import android.os.AsyncTask;


import com.yuan.yc_flutter_dynamic_old.BuildConfig;
import com.yuan.yc_flutter_dynamic_old.utils.FileUtil;
import com.yuan.yc_flutter_dynamic_old.utils.IOUtils;
import com.yuan.yc_flutter_dynamic_old.utils.ZipUtil;

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
public class DownloadTask extends AsyncTask<String, Integer, Boolean> {

    private DownloadListener mListener;

    public DownloadTask(DownloadListener listener) {
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

        if (strings == null || strings.length != 2) {
            if (mListener != null)
                mListener.onFailure(new Exception("arguments error"));
            return false;
        }

        String urlStr = strings[0];
        String filePath = strings[1];
        URL url;
        HttpURLConnection connection;

        FileOutputStream fos = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            String urlFilePath = connection.getURL().getFile();
            String fileName = urlFilePath.substring(urlFilePath.lastIndexOf(File.separatorChar) + 1);
            String subPath = fileName.substring(0, fileName.lastIndexOf("."));
            File file = new File(filePath + File.separator + subPath);

            if (!file.exists() && file.mkdirs()) {
                connection.setConnectTimeout(4000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Charset", "utf-8");
                connection.connect();
                fos = new FileOutputStream(new File(file, fileName));
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
            }

            String endDir = new File(file, fileName).getAbsolutePath();

            ZipUtil.UnZipFolder(endDir, file.getAbsolutePath());
            replaceSoFile(file.getAbsolutePath(), filePath);
            replaceAssetFile(endDir, filePath);

            return true;

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return false;
        } finally {
            IOUtils.closeIO(fos, is, bis);
        }

    }

    private void replaceSoFile(String path, String target) {
//        String arm = "armeabi-v7a"; // arm64-v8a
        String arm = "arm64-v8a";
        FileUtil.replaceSoFile(new File(path + File.separator + "jni" + File.separator + arm + File.separator +
                "libapp.so").getAbsolutePath(), target);
    }

    private void replaceAssetFile(String path, String target) {
        FileUtil.replaceAssetFile(new File(path).getAbsolutePath(), target);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mListener != null) {
            mListener.onProgress(values[0]);
        }
    }

    @Override
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
        void onStart();

        void onProgress(int progress);

        void onFailure(Exception e);

        void onSuccess();

    }
}
