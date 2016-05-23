package com.scrat.app.bus.model;

import java.util.List;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class LocationInfoItem {
    private List<LocationInfo> bl;
    private List<LocationInfo> bbl;

    public boolean isArrivaled() {
        return bl != null && bl.size() > 0;
    }

    public boolean isArrivaling() {
        return bbl != null && bbl.size() > 0;
    }

    @Override
    public String toString() {
        return "GetLocationInfoResponse{" +
                "bl=" + bl +
                ", bbl=" + bbl +
                '}';
    }
}
