package com.yuan.dynamic.net;

import android.os.AsyncTask;
import android.webkit.DownloadListener;

import com.yuan.dynamic.utils.FileUtil;
import com.yuan.dynamic.utils.ZipUtil;

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
        if (mListener != null) {
            mListener.onStart();
        }
        super.onPreExecute();
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
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
            String urlFilePath = connection.getURL().getFile();
            String fileName = urlFilePath.substring(urlFilePath.lastIndexOf(File.separatorChar) + 1);
            String subPath = fileName.substring(0, fileName.lastIndexOf("."));
            File file = new File(filePath + File.separator + subPath);

            if (!file.exists()) {
                file.mkdirs();
                FileOutputStream outputStream = new FileOutputStream(new File(file, fileName));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    int contentLength = connection.getContentLength();
                    BufferedInputStream bfi = new BufferedInputStream(inputStream);
                    int len;
                    int total = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = bfi.read(bytes)) != -1) {
                        total += len;
                        outputStream.write(bytes, 0, len);
                        float progress = total * 100f / contentLength;
                        publishProgress((int) progress);
                    }
                    outputStream.close();
                    inputStream.close();
                    bfi.close();
                }
            }
            publishProgress(100);
            String endDir = new File(file, fileName).getAbsolutePath();

            unzip(endDir, file.getAbsolutePath());
            replaceSoFile(file.getAbsolutePath(), filePath);
            replaceAssetFile(endDir, filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void unzip(String file, String path) throws Exception {
        File filePath = new File(path);
        if (!filePath.exists() || filePath.list().length <= 1) {
            ZipUtil.UnZipFolder(file, path);
        }
    }

    private void replaceSoFile(String path, String target) {
        FileUtil.replaceSoFile(new File(path + File.separator + "jni" + File.separator + "arm64-v8a" + File.separator + "libapp.so").getAbsolutePath(), target);
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
