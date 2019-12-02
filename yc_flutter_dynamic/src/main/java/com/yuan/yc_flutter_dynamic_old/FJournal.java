package com.yuan.yc_flutter_dynamic_old;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

import io.flutter.BuildConfig;

/**
 * Created by yuan on 2019-11-27.
 * Email:yuanwb@yiche.com
 */

@Deprecated
public class FJournal {

    public static final String DIRTY = "DIRTY";
    public static final String EMPTY_LINE = "\r\n";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String UNZIP = "UNZIP";
    public static final String REPLACE_SO = "REPLACE_SO";
    public static final String REPLACE_ASSETS = "REPLACE_ASSETS";
    public static final String FINISH = "FINISH";

    private static final String JOURNAL_NAME = "journal";

    private BufferedWriter journalWriter;

    private BufferedReader journalReader;

    private String mJournalFilePath;

    private static FJournal mJournal;

    private File journalFile;

    public static FJournal open(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            throw new RuntimeException("filePath must not empty!");
        }

        if (mJournal == null) {
            mJournal = new FJournal();
            mJournal.mJournalFilePath = filePath;
//            mJournal.generateWriter();
        }
        return mJournal;
    }

    private synchronized void generateWriter() {

        if (journalWriter != null) {
            return;
        }
        File journalFilePath = new File(mJournalFilePath);
        if (!journalFilePath.exists()) {
            boolean mkdirs = journalFilePath.mkdirs();
            if (!mkdirs) {
                if (BuildConfig.DEBUG) Log.d("FJournal", "dir journal create failure");
            }
        }
        try {
            journalFile = new File(journalFilePath, JOURNAL_NAME);
            Charset charset = Charset.forName("UTF-8");
            journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(journalFile, false), charset));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private synchronized void generateReader() {

        if (journalReader != null) {
            return;
        }

        File journalFilePath = new File(mJournalFilePath);
        if (journalFile == null || !journalFile.exists()) {
            if (!journalFilePath.exists()) {
                boolean mkdirs = journalFilePath.mkdirs();
                if (!mkdirs) {
                    if (BuildConfig.DEBUG) Log.d("FJournal", "dir journal create failure");
                }
            }
        }
        try {
            journalFile = new File(journalFilePath, JOURNAL_NAME);
            Charset charset = Charset.forName("UTF-8");
            FileInputStream fileInputStream = new FileInputStream(journalFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
            journalReader = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void write(String str) {
        generateWriter();
        try {
            journalWriter.write(str);
            journalWriter.newLine();
            journalWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> read() {
        generateReader();
        List<String> lines = new ArrayList<>();
        if (journalReader == null) {
            return lines;
        }
        try {
            String line;
            while ((line = journalReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public synchronized void close() {
//        CloseIoUtils.closeIO(journalWriter, journalReader);
    }
}
