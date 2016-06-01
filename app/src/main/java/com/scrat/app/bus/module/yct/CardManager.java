package com.scrat.app.bus.module.yct;

import android.nfc.tech.IsoDep;

import com.scrat.app.bus.model.NfcCardInfo;
import com.scrat.app.bus.model.NfcCardLog;
import com.scrat.app.core.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CardManager {

    private static final byte[] DFN_SRV = {(byte) 'P', (byte) 'A', (byte) 'Y',
            (byte) '.', (byte) 'A', (byte) 'P', (byte) 'P', (byte) 'Y',};

    private static final byte[] DFN_SRV_S1 = {(byte) 'P', (byte) 'A',
            (byte) 'Y', (byte) '.', (byte) 'P', (byte) 'A', (byte) 'S',
            (byte) 'D',};

    private static final byte[] DFN_SRV_S2 = {(byte) 'P', (byte) 'A',
            (byte) 'Y', (byte) '.', (byte) 'T', (byte) 'I', (byte) 'C',
            (byte) 'L',};

    private static final byte[] DFN_PSE = {(byte) '1', (byte) 'P',
            (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
            (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
            (byte) '0', (byte) '1',};

    private static final int MAX_LOG = 10;
    private static final int SFI_EXTRA = 21;
    private static final int SFI_LOG = 24;

    private static final byte TRANS_CSU = 6;
    private static final byte TRANS_CSU_CPX = 9;

    public static NfcCardInfo load(IsoDep isodep) {
        if (isodep == null)
            return NfcCardInfo.NULL;

        if (!YctHelper.selectByName(isodep, DFN_PSE).isOk())
            return NfcCardInfo.NULL;

        if (!YctHelper.selectByName(isodep, DFN_SRV).isOk())
            return NfcCardInfo.NULL;

        Iso7816.Response binaryInfo = YctHelper.readBinary(isodep, SFI_EXTRA);

        List<byte[]> logs = new ArrayList<>();
        if (YctHelper.selectByName(isodep, DFN_SRV_S1).isOk()) {
            logs.addAll(readLog(isodep, SFI_LOG));
        }

        if (YctHelper.selectByName(isodep, DFN_SRV_S2).isOk()) {
            logs.addAll(readLog(isodep, SFI_LOG));
        }

        NfcCardInfo info = new NfcCardInfo();

        if (binaryInfo.isOk() && binaryInfo.size() > 30) {
            final byte[] infoBytes = binaryInfo.getBytes();
            info.setCardId(Utils.toHexString(infoBytes, 11, 5));
            String beginDate = String.format("%02X%02X.%02X.%02X",
                    infoBytes[23], infoBytes[24], infoBytes[25], infoBytes[26]);
            info.setBeginDate(beginDate);
            String endDate = String.format("%02X%02X.%02X.%02X",
                    infoBytes[27], infoBytes[28], infoBytes[29], infoBytes[30]);
            info.setEndDate(endDate);
        }

        List<NfcCardLog> infos = parseLog(logs);
        info.setLogs(infos);

        return info;
    }

    private static List<NfcCardLog> parseLog(List<byte[]> logs) {
        List<NfcCardLog> infos = new ArrayList<>();
        if (logs == null)
            return infos;

        for (final byte[] v : logs) {
            float cash = Utils.toInt(v, 5, 4) / 100f;
            if (cash > 0) {
                if (v[9] == TRANS_CSU || v[9] == TRANS_CSU_CPX) {
                    cash *= -1f;
                }
                String date = String.format("%02X-%02X %02X:%02X ", v[18], v[19], v[20], v[21]);
                NfcCardLog info = new NfcCardLog(date, cash);
                infos.add(info);
            }
        }
        return infos;
    }

    private static List<byte[]> readLog(IsoDep isoDep, int sfi) {
        final List<byte[]> ret = new ArrayList<>();
        final Iso7816.Response rsp = YctHelper.readRecord(isoDep, sfi);
        if (rsp.isOk()) {
            addLog(rsp, ret);
        } else {
            for (int i = 1; i <= MAX_LOG; ++i) {
                if (!addLog(YctHelper.readRecord(isoDep, sfi, i), ret))
                    break;
            }
        }

        return ret;
    }

    private static boolean addLog(final Iso7816.Response r, List<byte[]> l) {
        if (!r.isOk())
            return false;

        final byte[] raw = r.getBytes();
        final int N = raw.length - 23;
        if (N < 0)
            return false;

        for (int s = 0, e; s <= N; s = e) {
            l.add(Arrays.copyOfRange(raw, s, (e = s + 23)));
        }

        return true;
    }
}
