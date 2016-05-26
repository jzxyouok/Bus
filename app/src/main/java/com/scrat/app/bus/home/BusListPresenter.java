package com.scrat.app.bus.home;

import com.google.gson.reflect.TypeToken;
import com.scrat.app.bus.model.BusStopInfo;
import com.scrat.app.bus.model.GetBusStopInfoResponse;
import com.scrat.app.bus.model.GetLocationResponse;
import com.scrat.app.bus.model.LocationInfoItem;
import com.scrat.app.bus.net.NetApi;
import com.scrat.app.core.net.GsonParser;
import com.scrat.app.core.net.ResponseCallback;
import com.scrat.app.core.utils.L;
import com.scrat.app.core.utils.NetUtil;
import com.scrat.app.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class BusListPresenter implements BusListContract.Presenter {
    private String mBusId;
    private boolean mDefaultOrder = true;
    private List<BusStopInfo> mBusStopInfoList;
    private BusListContract.View mView;

    public BusListPresenter(BusListContract.View view, String busId) {
        mBusId = busId;
        mView = view;
        mBusStopInfoList = new ArrayList<>();
    }

    @Override
    public void init() {
        if (!NetUtil.isNetworkAvailable()) {
            mView.onNoNetworkError();
            mView.hideLoading();
            return;
        }

        if (!Utils.isEmpty(mBusStopInfoList)) {
            loadLocation();
        } else {
            getBusStopName(new OnLoadBusStopNameListener() {
                @Override
                public void onLoadBusStopNameSuccess(List<BusStopInfo> busStopInfoList) {
                    mBusStopInfoList = busStopInfoList;
                    mView.showBusStop(mBusStopInfoList);
                    loadLocation();
                }
            });
        }
    }

    @Override
    public void changeOrder() {
        mDefaultOrder = !mDefaultOrder;
        mBusStopInfoList.clear();
        init();
    }

    private void getBusStopName(final OnLoadBusStopNameListener listener) {
        if (!NetUtil.isNetworkAvailable()) {
            mView.onNoNetworkError();
            mView.hideLoading();
            return;
        }

        mView.showLoading();
        NetApi.getBusStopName(mBusId, mDefaultOrder, new ResponseCallback<GetBusStopInfoResponse>() {
            @Override
            protected void onRequestSuccess(GetBusStopInfoResponse getBusStopInfoResponse) {
                mView.hideLoading();
                L.e("%s", getBusStopInfoResponse);
                List<BusStopInfo> busStopInfoList = getBusStopInfoResponse.getBusStopInfoList();
                if (listener == null)
                    return;

                listener.onLoadBusStopNameSuccess(busStopInfoList);
            }

            @Override
            protected GetBusStopInfoResponse parseResponse(Response response) {
                return GsonParser.getInstance().getGson()
                        .fromJson(response.body().charStream(), GetBusStopInfoResponse.class);
            }

            @Override
            protected void onRequestFailure(Exception e) {
                e.printStackTrace();
                mView.hideLoading();
                mView.onLoadDataError();
            }
        });
    }

    private void loadLocation() {
        if (!NetUtil.isNetworkAvailable()) {
            mView.onNoNetworkError();
            mView.hideLoading();
            return;
        }

        mView.showLoading();
        NetApi.getBusLocation(mBusId, mDefaultOrder, new ResponseCallback<GetLocationResponse>() {
            @Override
            protected void onRequestSuccess(GetLocationResponse response) {
                mView.hideLoading();
                List<LocationInfoItem> itemList = response.getLocationList();
                if (Utils.isEmpty(itemList))
                    return;

                int totalSize = itemList.size();
                for (int i = 0; i < totalSize; i++) {
                    LocationInfoItem locationInfoItem = itemList.get(i);
                    BusStopInfo busStopInfo = mBusStopInfoList.get(i);
                    busStopInfo.setArrivaled(locationInfoItem.isArrivaled());
                    busStopInfo.setLeaving(locationInfoItem.isLeaving());
                }

                mView.showBusStop(mBusStopInfoList);
            }

            @Override
            protected GetLocationResponse parseResponse(Response response) {

                List<LocationInfoItem> itemList = GsonParser.getInstance().getGson()
                        .fromJson(response.body().charStream(), new TypeToken<List<LocationInfoItem>>() {
                        }.getType());

                return new GetLocationResponse(itemList);
            }

            @Override
            protected void onRequestFailure(Exception e) {
                e.printStackTrace();
                mView.hideLoading();
                mView.onLoadDataError();
            }
        });
    }

    /*package*/ interface OnLoadBusStopNameListener {
        void onLoadBusStopNameSuccess(List<BusStopInfo> busStopInfoList);
    }
}
