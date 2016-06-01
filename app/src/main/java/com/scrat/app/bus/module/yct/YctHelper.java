package com.scrat.app.bus.module.yct;

import android.nfc.tech.IsoDep;

import java.nio.ByteBuffer;

/**
 * Created by yixuanxuan on 16/6/1.
 */
public class YctHelper {
    public static Iso7816.Response readRecord(IsoDep isoDep, int sfi, int index) {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) index, // P1 Parameter 1
                (byte) ((sfi << 3) | 0x04), // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return new Iso7816.Response(transceive(isoDep, cmd));
    }

    public static Iso7816.Response readRecord(IsoDep isoDep, int sfi) {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) 0x01, // P1 Parameter 1
                (byte) ((sfi << 3) | 0x05), // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return new Iso7816.Response(transceive(isoDep, cmd));
    }

    private static byte[] transceive(IsoDep isoDep, final byte[] cmd) {
        try {
            return isoDep.transceive(cmd);
        } catch (Exception e) {
            return Iso7816.Response.ERROR;
        }
    }

    public static Iso7816.Response readBinary(IsoDep isoDep, int sfi) {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB0, // INS Instruction
                (byte) (0x00000080 | (sfi & 0x1F)), // P1 Parameter 1
                (byte) 0x00, // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return new Iso7816.Response(transceive(isoDep, cmd));
    }

    public static Iso7816.Response selectByName(IsoDep isoDep, byte... name) {
        ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
        buff.put((byte) 0x00) // CLA Class
                .put((byte) 0xA4) // INS Instruction
                .put((byte) 0x04) // P1 Parameter 1
                .put((byte) 0x00) // P2 Parameter 2
                .put((byte) name.length) // Lc
                .put(name).put((byte) 0x00); // Le

        return new Iso7816.Response(transceive(isoDep, buff.array()));
    }
}
