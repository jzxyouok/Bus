package com.scrat.app.bus.model;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/21.
 */
public class GetBusInfoResponse {
    private List<BusInfo> busInfos;

    public GetBusInfoResponse(List<BusInfo> busInfos) {
        this.busInfos = busInfos;
    }

    public List<BusInfo> getBusInfos() {
        return busInfos;
    }

    public void setBusInfos(List<BusInfo> busInfos) {
        this.busInfos = busInfos;
    }

    @Override
    public String toString() {
        return "GetBusInfoResponse{" +
                "busInfos=" + busInfos +
                '}';
    }
}
