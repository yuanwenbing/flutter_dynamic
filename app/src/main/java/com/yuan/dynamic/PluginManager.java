package com.yuan.dynamic;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuan on 2019-11-15.
 * Email:yuanwb@yiche.com
 */
public class PluginManager {

    public static boolean isNeedReplace(Context context, String fileName) {

        try {
            File ofile = new File(context.getFilesDir(), "libapp.so");
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);

            Log.d("PluginManager", "ofile:" + ofile.length());
            Log.d("PluginManager", "inputStream.available():" + inputStream.available());
            if (ofile.length() == inputStream.available()) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void replaceResFile(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            File filesDir = context.getFilesDir();
            File file = new File(filesDir, fileName);
            if (file.exists()) {
                boolean delete = file.delete();
                if (delete) {
                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                }
            }
            outputStream = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void replaceSoFile(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            File filesDir = context.getFilesDir();
            File file = new File(filesDir, "libapp.so");
            if (file.exists()) {
                boolean delete = file.delete();
                if (delete) {
                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                }
            }
            outputStream = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyAssetsDir2Phone(Context context, String filePath) {
        try {
            String[] fileList = context.getAssets().list(filePath);
            if (fileList != null && fileList.length > 0) {//如果是目录
                File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + filePath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileList) {
                    filePath = filePath + File.separator + fileName;
                    copyAssetsDir2Phone(context, filePath);
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
                    Log.e("oldPath", filePath);
                }
            } else {//如果是文件
                InputStream inputStream = context.getAssets().open(filePath);
                File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + filePath);
                Log.i("copyAssets2Phone", "file:" + file);
                if (!file.exists() || file.length() == 0) {
                    FileOutputStream fos = new FileOutputStream(file);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
