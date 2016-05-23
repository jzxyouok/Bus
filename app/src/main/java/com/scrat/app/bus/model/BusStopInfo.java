package com.scrat.app.bus.model;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class BusStopInfo {
    private String i;
    private String n;
    private String sni;
    private String si;

    private boolean isArrivaled;
    private boolean isLeaving;

    public String getBusStopName() {
        return n;
    }

    public boolean isArrivaled() {
        return isArrivaled;
    }

    public void setArrivaled(boolean arrivaled) {
        isArrivaled = arrivaled;
    }

    public boolean isLeaving() {
        return isLeaving;
    }

    public void setLeaving(boolean leaving) {
        isLeaving = leaving;
    }

    @Override
    public String toString() {
        return "BusStopInfo{" +
                "i='" + i + '\'' +
                ", n='" + n + '\'' +
                ", sni='" + sni + '\'' +
                ", si='" + si + '\'' +
                ", isArrivaled=" + isArrivaled +
                ", isLeaving=" + isLeaving +
                '}';
    }
}
