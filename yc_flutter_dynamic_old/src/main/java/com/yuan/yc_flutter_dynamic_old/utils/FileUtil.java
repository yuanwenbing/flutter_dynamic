package com.yuan.yc_flutter_dynamic_old.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.yuan.yc_flutter_dynamic_old.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by yuan on 2019-11-15.
 * Email:yuanwb@yiche.com
 */
public class FileUtil {

    public static String getAarPath(Context context) {
        File filesDir = context.getFilesDir();
        File filePath = new File(filesDir + File.separator + "p");
        if (filePath.exists()) {
            filePath.mkdir();
        }
        return filePath.getAbsolutePath();
    }

    public static boolean isNeedReplace(Context context, String fileName) {

        try {
            File ofile = new File(context.getFilesDir(), "libapp.so");
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);

            Log.d("FileUtil", "ofile:" + ofile.length());
            Log.d("FileUtil", "inputStream.available():" + inputStream.available());
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

    public static void removeSoFile(Context context, String fileName) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, fileName);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void replaceSoFile(String fileName, String target) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {

            File sourceFile = new File(fileName);

            File file = new File(target).getParentFile();
            file = new File(file, "libapp.so");

            if (DiffUtil.check(file, sourceFile)) {
                if (BuildConfig.DEBUG) Log.d("FileUtil", "SoFile is same");
                return;
            }
            inputStream = new FileInputStream(sourceFile);
            if (file.exists()) {
                boolean delete = file.delete();
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

    public static void replaceAssetFile(String fileName, String target) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            File sourceFile = new File(fileName);
            File file = new File(target).getParentFile();
            file = new File(file, "res.apk");

            if (DiffUtil.check(file, sourceFile)) {
                if (BuildConfig.DEBUG) Log.d("FileUtil", "AssetFile is same");
                return;
            }
            inputStream = new FileInputStream(sourceFile);

            if (file.exists()) {
                boolean delete = file.delete();
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
            }
            outputStream = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            Toast.makeText(context, "replace success", Toast.LENGTH_SHORT).show();

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

    public static void copyAssetsDir2Phone(Context context, String filePath) {
        try {
            String[] fileList = context.getAssets().list(filePath);
            if (fileList != null && fileList.length > 0) {//如果是目录
                File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + filePath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileList) {
                    filePath = filePath + File.separator + fileName;
                    copyAssetsDir2Phone(context, filePath);
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
                }
            } else {//如果是文件
                InputStream inputStream = context.getAssets().open(filePath);
                File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + filePath);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static @NonNull
    String getFileMD5(File file) {
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
