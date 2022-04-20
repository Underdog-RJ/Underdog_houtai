package com.atguigu.pierce.netty.metrics;

import lombok.Data;

@Data
public class Metrics {
    private static final long serialVersionUID = 1L;

    private int port;

    private long readBytes;

    private long wroteBytes;

    private long readMsgs;

    private long wroteMsgs;

    private int channels;

    private long timestamp;
}

