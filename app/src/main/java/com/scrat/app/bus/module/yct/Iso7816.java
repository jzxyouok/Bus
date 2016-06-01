package com.scrat.app.bus.module.yct;

import java.util.Arrays;

public class Iso7816 {
    public static final byte[] EMPTY = {0};

    protected byte[] data;

    protected Iso7816(byte[] bytes) {
        data = (bytes == null) ? Iso7816.EMPTY : bytes;
    }

    public int size() {
        return data.length;
    }

    public byte[] getBytes() {
        return data;
    }

    public final static class Response extends Iso7816 {
        public static final byte[] EMPTY = {};
        public static final byte[] ERROR = {0x6F, 0x00}; // SW_UNKNOWN

        public Response(byte[] bytes) {
            super((bytes == null || bytes.length < 2) ? Response.ERROR : bytes);
        }

        public short getSw12() {
            final byte[] d = this.data;
            int n = d.length;
            return (short) ((d[n - 2] << 8) | (0xFF & d[n - 1]));
        }

        public boolean isOk() {
            return equalsSw12(SW_NO_ERROR);
        }

        public boolean equalsSw12(short val) {
            return getSw12() == val;
        }

        public int size() {
            return data.length - 2;
        }

        public byte[] getBytes() {
            return isOk() ? Arrays.copyOfRange(data, 0, size()) : Response.EMPTY;
        }
    }

    public static final short SW_NO_ERROR = (short) 0x9000;
}
