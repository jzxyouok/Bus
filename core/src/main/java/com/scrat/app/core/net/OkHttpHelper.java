package com.scrat.app.core.net;

import android.text.TextUtils;

import com.scrat.app.est.EstApp;
import com.scrat.app.est.utils.L;
import com.scrat.app.est.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yixuanxuan on 16/1/15.
 */
public class OkHttpHelper {
    private static class SingletonHolder {
        private static OkHttpHelper instance = new OkHttpHelper();
    }

    public static OkHttpHelper getInstance() {
        return SingletonHolder.instance;
    }

    private OkHttpClient client;

    private OkHttpHelper() {
        client = new OkHttpClient();
    }

    private Request buildPostFormRequest(String url, Map<String, String> params)
            throws UnsupportedEncodingException {

        FormBody.Builder builder = new FormBody.Builder();
        if (!isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addEncoded(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url).post(requestBody);

        return reqBuilder.build();
    }

    private Request buildGetFormRequest(
            String url, Map<String, String> headers, Map<String, String> params)
            throws UnsupportedEncodingException {
        if (!isEmpty(params)) {
            StringBuilder sb = new StringBuilder(url);
            sb.append('?');
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            url = sb.toString();
        }
        Request.Builder builder = new Request.Builder().url(url);
        if (!isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private Request buildMultipartFormRequest(
            String url, Map<String, File> files, Map<String, String> params) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addFormDataPart(key, value);
            }
        }

        if (!isEmpty(files)) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String key = entry.getKey();
                File file = files.get(key);
                String fileName = file.getName();
                RequestBody fileBody =
                        RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addFormDataPart(key, fileName, fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private String getResponseBody(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response.code() + ", " + response.message());
        }

        String encoding = "UTF-8";
        String contextType = response.headers().get("Content-Type");
        if (!TextUtils.isEmpty(contextType)) {
            if (contextType.toUpperCase().contains("UTF")) {
                encoding = "UTF-8";
            } else if (contextType.toUpperCase().contains("GBK")) {
                encoding = "GBK";
            }
        }
        return new String(response.body().bytes(), encoding);
    }

    private Response getResponse(Request request) throws IOException {
        int maxRetryTimes = 3;
        for (int i = 0; i < maxRetryTimes; i++) {
            try {
                return client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                int totalRequestTimes = i + 1;
                L.d("retry %s", totalRequestTimes);
                if (!NetUtil.isNetworkAvailable(EstApp.getContext())
                        || totalRequestTimes == maxRetryTimes) {
                    throw e;
                }
            }
        }

        throw new IOException("retry fail");
    }


    public String post(String url, Map<String, String> params) throws IOException {
        L.d("url %s", url);
        L.d("post %s", params);
        Request request = buildPostFormRequest(url, params);
        Response response = getResponse(request);
        L.d("%s", response.code());
        String body = getResponseBody(response);
        L.d("response %s", body);
        return body;
    }

    public String post(String url) throws IOException {
        return post(url, null);
    }

    public void post(String url, Map<String, String> params, Callback responseCallback)
            throws UnsupportedEncodingException {
        L.d("url %s", url);
        L.d("post %s", params);
        Request request = buildPostFormRequest(url, params);
        client.newCall(request).enqueue(responseCallback);
    }

    public String get(String url, Map<String, String> params)
            throws IOException {
        L.d("[get] %s", url);
        L.d("[params] %s", params);
        Request request = buildGetFormRequest(url, null, params);
        Response response = getResponse(request);
        L.d("%s", response.code());
        String body = getResponseBody(response);
        L.d("[response] %s", body);
        return body;
    }

    public String get(String url) throws IOException {
        return get(url, Collections.<String, String>emptyMap());
    }

    public void get(String url, Map<String, String> params,
                    Map<String, String> headers, Callback responseCallback)
            throws UnsupportedEncodingException {
        L.d("[get] %s", url);
        L.d("[params] %s", params);
        Request request = buildGetFormRequest(url, headers, params);
        client.newCall(request).enqueue(responseCallback);
    }

    public void get(String url, Callback responseCallback) throws UnsupportedEncodingException {
        get(url, null, null, responseCallback);
    }

    private boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    private boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    public static class ParamsBuilder {
        Map<String, String> params;

        public ParamsBuilder() {
            this.params = new HashMap<>();
        }

        public ParamsBuilder put(String key, String value) {
            params.put(key, value);
            return this;
        }

        public ParamsBuilder put(String key, long value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        public ParamsBuilder put(String key, int value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        public ParamsBuilder put(String key, boolean value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        public Map<String, String> build() {
            return params;
        }
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/xml");

    public void put(String url, String path) {
        final File file = new File(path);

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        try {
            Response response = getResponse(request);
            L.e("code=%s", response.code());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // this is hack ! Only use for upload feedback.
    public void uploadToFeedback(
            String url, String fileKey, List<String> paths, Map<String, String> params,
            Callback responseCallback) throws IOException {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (!isEmpty(paths)) {
            for (String path : paths) {
                final File file = new File(path);
                builder.addFormDataPart(
                        fileKey, file.getName(), RequestBody.create(MultipartBody.FORM, file));
            }
        }

        if (!isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(responseCallback);
    }

    public void download(String url, SaveFileResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
