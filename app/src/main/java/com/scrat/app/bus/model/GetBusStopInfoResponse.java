package com.scrat.app.bus.model;

import java.util.List;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class GetBusStopInfoResponse {
    private String rn;
    private String d;
    private String c;
    private String ft;
    private String lt;
    private List<BusStopInfo> l;

    public List<BusStopInfo> getBusStopInfoList() {
        return l;
    }

    @Override
    public String toString() {
        return "GetBusStopInfoResponse{" +
                "rn='" + rn + '\'' +
                ", d='" + d + '\'' +
                ", c='" + c + '\'' +
                ", ft='" + ft + '\'' +
                ", lt='" + lt + '\'' +
                ", l=" + l +
                '}';
    }
}
