package com.scrat.app.core.net;

import com.scrat.app.core.utils.ActivityUtils;
import com.scrat.app.core.utils.IoUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yixuanxuan on 16/5/10.
 */
public abstract class DownloadCallback implements Callback {
    private static final int sDefaultReadSize = 1024;

    protected abstract String getTargetFilePath();

    protected abstract void onRequestFailure(IOException e);

    protected void onDownloadSuccess() {
        // do nothing
    }

    protected void onProgress(int process, int total) {
        // do nothing
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        ActivityUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onRequestFailure(e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        final InputStream in = response.body().byteStream();
        OutputStream os = null;
        try {
            int total = getContentLength(response);
            File file = new File(getTargetFilePath());
            byte[] buffer = new byte[sDefaultReadSize];
            int bytesRead;
            int totalRead = 0;
            os = new FileOutputStream(file);
            while ((bytesRead = in.read(buffer, 0, sDefaultReadSize)) != -1) {
                os.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                notifyProgress(totalRead, total);
            }

            notifyDownloadSuccess();
        } finally {
            IoUtil.close(os);
            IoUtil.close(in);
        }

    }

    private int getContentLength(Response response) {
        String length = response.header("Content-Length");
        return Integer.parseInt(length);
    }

    private void notifyProgress(final int progress, final int total) {
        ActivityUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onProgress(progress, total);
            }
        });
    }

    private void notifyDownloadSuccess() {
        ActivityUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onDownloadSuccess();
            }
        });
    }

}
