package com.scrat.app.bus.home;

import com.scrat.app.bus.model.BusStopInfo;
import com.scrat.app.core.common.BaseView;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/12.
 */
public interface BusListContract {
    interface Presenter {
        void init();
        void changeOrder();
    }

    interface View extends BaseView {
        void showBusName(String busName);
        void showBusStop(List<BusStopInfo> list);
        void onLoadDataError();
    }
}
