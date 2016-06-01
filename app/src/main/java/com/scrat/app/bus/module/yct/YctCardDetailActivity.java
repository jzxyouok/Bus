package com.scrat.app.bus.module.yct;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseActivity;
import com.scrat.app.bus.model.NfcCardInfo;
import com.scrat.app.bus.model.NfcCardLog;
import com.scrat.app.core.common.BaseRecyclerViewAdapter;
import com.scrat.app.core.common.BaseRecyclerViewHolder;

import java.io.IOException;

/**
 * Created by yixuanxuan on 16/5/31.
 */
public class YctCardDetailActivity extends BaseActivity implements View.OnClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private TextView mCardIdTv;
    private RecyclerView mRecyclerView;
    private Button mBalanceBtn;
    private MyAdapter mAdapter;
    private ImageView mBackIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_yct_detail);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this.getApplicationContext());
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mBalanceBtn = (Button) findViewById(R.id.tv_balance);
        mBalanceBtn.setOnClickListener(this);
        mCardIdTv = (TextView) findViewById(R.id.tv_card_id);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        loadData(getIntent());
    }

    private IntentFilter[] getIntentFilter() {
        try {
            return new IntentFilter[]{new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[][] getTechLists() {
        return new String[][]{
                {IsoDep.class.getName()},
                {NfcV.class.getName()},
                {NfcF.class.getName()},};
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            IntentFilter[] filters = getIntentFilter();
            if (filters == null)
                return;

            String[][] techLists = getTechLists();
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, filters, techLists);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadData(intent);
    }

    private void loadData(Intent intent) {
        String action = intent.getAction();
        if (action == null)
            return;

        if (!action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) &&
                !action.equals(NfcAdapter.ACTION_TECH_DISCOVERED))
            return;

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final IsoDep isodep = IsoDep.get(tag);
        try {
            isodep.connect();
            NfcCardInfo cardInfo = CardManager.load(isodep);
            showData(cardInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isodep.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showData(NfcCardInfo cardInfo) {
        mCardIdTv.setVisibility(View.VISIBLE);
        mCardIdTv.setText(String.valueOf(cardInfo.getCardId()));
        mAdapter.setList(cardInfo.getLogs());
    }

    @Override
    public void onClick(View v) {
        if (v == mBackIv) {
            finish();
        } else if (v == mBalanceBtn) {
            YctCardBalanceActivity.startActivity(YctCardDetailActivity.this, mCardIdTv.getText().toString());
        }
    }

    private static class MyAdapter extends BaseRecyclerViewAdapter<NfcCardLog, BaseRecyclerViewHolder> {

        @Override
        protected void initView(BaseRecyclerViewHolder holder, int position, NfcCardLog nfcCardLog) {
            holder.setText(R.id.tv_rate, nfcCardLog.getRate())
                    .setText(R.id.tv_date, nfcCardLog.getDate())
                    .setText(R.id.tv_type, nfcCardLog.getType());
        }

        @Override
        public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_nfc_rate, parent, false);
            return new BaseRecyclerViewHolder(view);
        }
    }

}
