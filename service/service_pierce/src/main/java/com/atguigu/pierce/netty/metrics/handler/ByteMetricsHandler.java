package com.atguigu.pierce.netty.metrics.handler;

import com.atguigu.pierce.netty.metrics.MetricsCollector;
import com.atguigu.pierce.utils.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.swagger.models.auth.In;

import java.net.InetSocketAddress;

public class ByteMetricsHandler extends ChannelDuplexHandler {


    MetricsCollector metricsCollector = SpringUtil.getBean(MetricsCollector.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        metricsCollector.incrementReadInfo(sa.getPort(), ((ByteBuf) msg).readableBytes());
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        metricsCollector.incrementWriteInfo(sa.getPort(), ((ByteBuf) msg).readableBytes());
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        metricsCollector.incrementChannelNum(sa.getPort());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        metricsCollector.decrementChannelNum(sa.getPort());
        super.channelInactive(ctx);
    }
}
