package com.yuan.yc_flutter_dynamic.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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
            boolean mkdir = filePath.mkdir();
            if (!mkdir) {
                Log.d("FileUtil", "mkdir failure");
            }
        }
        return filePath.getAbsolutePath();
    }

    public static void replaceSoFile(String fileName, String target) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {

            File sourceFile = new File(fileName);

            File file = new File(target).getParentFile();
            file = new File(file, "libapp.so");

            inputStream = new FileInputStream(sourceFile);
            if (file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    Log.d("FileUtil", "delete failure");
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

    public static void replaceAssetFile(String fileName, String target) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            File sourceFile = new File(fileName);
            File file = new File(target).getParentFile();
            file = new File(file, "res.apk");

            inputStream = new FileInputStream(sourceFile);

            if (file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    Log.d("FileUtil", "delete failure");
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
            CloseIoUtils.closeIO(inputStream, outputStream);
        }
    }


    @NonNull
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
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

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
