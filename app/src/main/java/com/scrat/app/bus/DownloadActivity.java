package com.scrat.app.bus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.scrat.app.core.utils.IoUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class DownloadActivity extends AppCompatActivity {
    private TextView mTotalTv;
    private TextView mProcessTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        initView();

        initData();
    }

    private void initData() {
        String url = "http://mirrors.cnnic.cn/apache/tomcat/tomcat-9/v9.0.0.M6/bin/apache-tomcat-9.0.0.M6.zip";
        String path = "/sdcard/Download/a.zip";
        new DownloadAsyncTask(url, path, new IDownloadListener() {
            @Override
            public void process(long size) {
                mProcessTv.setText(String.valueOf(size));
            }

            @Override
            public void done() {
                Toast.makeText(DownloadActivity.this, "下载完毕", Toast.LENGTH_LONG).show();
            }
        });

        new AsyncTask<Long, Integer, Integer>() {
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mProcessTv.setText(String.valueOf(values[0]));
            }

            @Override
            protected Integer doInBackground(Long... params) {
                for (int i = 60; i >= 0; i--) {
                    publishProgress(i);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    private void initView() {
        mTotalTv = (TextView) findViewById(R.id.total_size);
        mProcessTv = (TextView) findViewById(R.id.process_size);
    }

    interface IDownloadListener {
        void process(long size);
        void done();
    }

    private class DownloadAsyncTask extends AsyncTask<Void, Long, Long> {
        private String mPath;
        private String mUrl;
        IDownloadListener mDownloadListener;

        public DownloadAsyncTask(String url, String path, IDownloadListener downloadListener) {
            mUrl = url;
            mPath = path;
            mDownloadListener = downloadListener;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            mDownloadListener.process(values[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            mDownloadListener.done();
        }

        @Override
        protected Long doInBackground(Void... params) {
            FileOutputStream os = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                File file = new File(mPath);
                os = new FileOutputStream(file);

                Long totalRead = 0L;
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                    os.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
//                    publishProgress(totalRead);
                    mDownloadListener.process(totalRead);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IoUtil.close(os);
            }

            return null;
        }
    }



}
