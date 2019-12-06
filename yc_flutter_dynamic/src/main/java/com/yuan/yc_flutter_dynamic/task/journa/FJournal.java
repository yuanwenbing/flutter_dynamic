package com.yuan.yc_flutter_dynamic.task.journa;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.yuan.yc_flutter_dynamic.utils.CloseIoUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import io.flutter.BuildConfig;

/**
 * Created by yuan on 2019-11-27.
 * Email:yuanwb@yiche.com
 * <p>
 */

public class FJournal {

    public static final String DIRTY = "DIRTY";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String UNZIP = "UNZIP";
    public static final String REPLACE_SO = "REPLACE_SO";
    public static final String REPLACE_ASSETS = "REPLACE_ASSETS";
    public static final String SUCCESS = "SUCCESS";

    private static final String JOURNAL_NAME = "journal";

    private String mJournalFilePath;

    private static FJournal mJournal;

    public static FJournal open(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath must not empty!");
        }

        if (mJournal == null) {
            synchronized (FJournal.class) {
                if (mJournal == null) {
                    mJournal = new FJournal();
                    mJournal.mJournalFilePath = filePath;
                }
            }
        }
        return mJournal;
    }

    @WorkerThread
    public synchronized void write(String str) {
        File journalFilePath = new File(mJournalFilePath);
        if (!journalFilePath.exists()) {
            boolean mkdirs = journalFilePath.mkdirs();
            if (!mkdirs) {
                if (BuildConfig.DEBUG) Log.d("FJournal", "dir journal create failure");
            }
        }
        BufferedWriter journalWriter = null;
        try {
            File journalFile = new File(journalFilePath, JOURNAL_NAME);
            Charset charset = Charset.forName("UTF-8");
            journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(journalFile, false), charset));
            journalWriter.write(str);
            journalWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseIoUtils.closeIO(journalWriter);
        }

    }

    @WorkerThread
    public synchronized @Nullable
    String read() {
        BufferedReader journalReader = null;
        try {
            File journalFilePath = new File(mJournalFilePath);
            File journalFile = new File(journalFilePath, JOURNAL_NAME);
            Charset charset = Charset.forName("UTF-8");
            FileInputStream fileInputStream = new FileInputStream(journalFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
            journalReader = new BufferedReader(inputStreamReader);
            return journalReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseIoUtils.closeIO(journalReader);
        }
        return "";
    }
}
