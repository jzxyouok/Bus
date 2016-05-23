package com.scrat.app.bus.model;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/14.
 */
public class GetLocationResponse {
    public GetLocationResponse(List<LocationInfoItem> locationList) {
        this.locationList = locationList;
    }

    private List<LocationInfoItem> locationList;

    public List<LocationInfoItem> getLocationList() {
        return locationList;
    }

    @Override
    public String toString() {
        return "GetLocationResponse{" +
                "locationList=" + locationList +
                '}';
    }
}
