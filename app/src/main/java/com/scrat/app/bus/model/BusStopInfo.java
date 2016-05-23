package com.scrat.app.bus.model;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class BusStopInfo {
    private String i;
    private String n;
    private String sni;
    private String si;

    public String getBusStopName() {
        return n;
    }

    @Override
    public String toString() {
        return "BusStopInfo{" +
                "i='" + i + '\'' +
                ", n='" + n + '\'' +
                ", sni='" + sni + '\'' +
                ", si='" + si + '\'' +
                '}';
    }
}
