package com.scrat.app.bus.model;

/**
 * Created by yixuanxuan on 16/6/1.
 */
public class NfcCardLog {
    private String date;
    private float money;

    public NfcCardLog(String date, float money) {
        this.date = date;
        this.money = money;
    }

    public String getType() {
        if (this.money > 0f)
            return "充值";
        return "消费";
    }

    public String getRate() {
        if (money > 0f) {
            return String.format("+%.2f元", money);
        }
        return String.format("-%.2f元", money);
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "NfcCardInfo{" +
                "date='" + date + '\'' +
                ", money=" + money +
                '}';
    }
}
