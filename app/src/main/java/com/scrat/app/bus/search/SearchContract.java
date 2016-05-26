package com.scrat.app.bus.search;

import com.scrat.app.bus.model.BusInfo;
import com.scrat.app.core.common.BaseView;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/21.
 */
public interface SearchContract {
    interface Presenter {
        void search(String content);
    }

    interface View extends BaseView {
        void showNoResult();
        void showResult(List<BusInfo> busInfos);
        void onSearchError();
        void onContentEmptyError();
    }
}
