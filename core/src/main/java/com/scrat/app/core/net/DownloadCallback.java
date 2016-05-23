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

    protected abstract String buildTargetFilePath();

    protected abstract void onRequestFailure(IOException e);

    protected abstract void onDownloadSuccess();

    protected void onProgress(long totalReadSize, long totalSize) {
        // do nothing
    }

    /**
     * 是否需要回掉进度
     * @param totalReadSize 当前已经读取的大小
     * @param totalSize 网络文件大小
     * @param bytesRead 本次读取的大小
     * @return true: 回掉进度. false(default): 不回调进度.
     */
    protected boolean shouldNotifyProgress(long totalReadSize, long totalSize, int bytesRead) {
        return false;
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

        byte[] buffer = new byte[sDefaultReadSize];
        final InputStream in = response.body().byteStream();
        OutputStream os = null;
        try {
            long total = getContentLength(response);
            File file = new File(buildTargetFilePath());
            int bytesRead;
            long totalRead = 0L;
            os = new FileOutputStream(file);
            while ((bytesRead = in.read(buffer, 0, sDefaultReadSize)) != -1) {
                os.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                if (shouldNotifyProgress(totalRead, total, bytesRead)) {
                    notifyProgress(totalRead, total);
                }
            }

            notifyDownloadSuccess();
        } finally {
            IoUtil.close(os);
            IoUtil.close(in);
        }

    }

    private long getContentLength(Response response) {
        String length = response.header("Content-Length");
        return Long.parseLong(length);
    }

    private void notifyProgress(final long totalReadSzie, final long totalSize) {
        ActivityUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onProgress(totalReadSzie, totalSize);
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
