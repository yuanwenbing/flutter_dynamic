package com.yuan.yc_flutter_dynamic_old.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by yuan on 2019-11-27.
 * Email:yuanwb@yiche.com
 */
public class CloseIoUtils {
    public static void closeIO(Closeable... streams) {
        for (Closeable stream : streams) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
