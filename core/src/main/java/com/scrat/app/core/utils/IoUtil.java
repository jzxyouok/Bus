package com.scrat.app.core.utils;

import android.database.Cursor;

import java.io.Closeable;

public class IoUtil {

    private IoUtil() {
    }

    public static void close(Closeable closeable) {
        if (closeable == null)
            return;

        try {
            closeable.close();
        } catch (Exception ignore) {
        }
    }

    /**
     * Android's Cursor did not implement Closeable until API Level 16
     */
    public static void close(Cursor cursor) {
        if (cursor == null)
            return;

        try {
            cursor.close();
        } catch (Exception ignore) {
        }
    }
}
