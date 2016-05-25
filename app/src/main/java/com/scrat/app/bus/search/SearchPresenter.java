package com.scrat.app.bus.search;

import com.google.gson.reflect.TypeToken;
import com.scrat.app.bus.model.BusInfo;
import com.scrat.app.bus.net.NetApi;
import com.scrat.app.core.net.GsonParser;
import com.scrat.app.core.net.ResponseCallback;
import com.scrat.app.core.utils.NetUtil;
import com.scrat.app.core.utils.Utils;

import java.util.List;

import okhttp3.Response;

/**
 * Created by yixuanxuan on 16/5/21.
 */
public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mView;
    public SearchPresenter(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void search(String content) {
        if (!NetUtil.isNetworkAvailable()) {
            mView.onNoNetworkError();
            return;
        }

        NetApi.getBusInfo(content, new ResponseCallback<List<BusInfo>>() {
            @Override
            protected void onRequestSuccess(List<BusInfo> busInfos) {
                if (Utils.isEmpty(busInfos)) {
                    mView.showNoResult();
                    return;
                }

                mView.showResult(busInfos);
            }

            @Override
            protected List<BusInfo> parseResponse(Response response) {
                return GsonParser.getInstance().getGson()
                        .fromJson(response.body().charStream(),
                                new TypeToken<List<BusInfo>>(){}.getType());
            }

            @Override
            protected void onRequestFailure(Exception e) {
                e.printStackTrace();
                mView.onSearchError();
            }
        });
    }
}
