package com.scrat.app.bus.model;

import java.util.List;

/**
 * Created by yixuanxuan on 16/6/1.
 */
public class NfcCardInfo {
    private String cardId;
    private List<NfcCardLog> logs;
    private String beginDate;
    private String endDate;
    private float balance;

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setLogs(List<NfcCardLog> logs) {
        this.logs = logs;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCardId() {
        return cardId;
    }

    public List<NfcCardLog> getLogs() {
        return logs;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public static final NfcCardInfo NULL = new NfcCardInfo();
}
