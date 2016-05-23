package com.scrat.app.core.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scrat.app.core.utils.L;

/**
 * Created by yixuanxuan on 16/3/31.
 */
public class GsonParser {
    private static class SingletonHolder {
        private static GsonParser instance = new GsonParser();
    }

    public static GsonParser getInstance(){
        return SingletonHolder.instance;
    }

    private Gson gson;

    private GsonParser() {
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            L.e(e);
            return null;
        }
    }
}
