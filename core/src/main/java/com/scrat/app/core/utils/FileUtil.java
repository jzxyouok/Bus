package com.scrat.app.core.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class FileUtil {
    private FileUtil() {
    }

    public static void getDownloadPath() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; ++i) {
            Log.e("fileutil", files[i].getAbsolutePath());
        }
    }
}
