package com.atguigu.pierce.netty.handlers;

import com.atguigu.pierce.entity.Client;
import com.atguigu.pierce.mapper.ClientMapper;
import com.atguigu.pierce.netty.ProxyChannelManager;
import com.atguigu.pierce.netty.config.ProxyConfig;
import com.atguigu.pierce.netty.metrics.handler.ByteMetricsHandler;
import com.atguigu.pierce.netty.protocol.Constants;
import com.atguigu.pierce.netty.protocol.ProxyMessage;
import com.atguigu.pierce.service.PierceService;
import com.atguigu.pierce.utils.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.BindException;
import java.util.List;

public class ServerChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {

    private static Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);

    private volatile NioEventLoopGroup serverWorkerGroup;

    private volatile NioEventLoopGroup serverBossGroup;

    private volatile ServerBootstrap bootstrap = new ServerBootstrap();

    public ServerChannelHandler(NioEventLoopGroup boss, NioEventLoopGroup worker) {
        this.serverWorkerGroup = worker;
        this.serverBossGroup = boss;
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addFirst(new ByteMetricsHandler());
                        ch.pipeline().addLast(new UserChannelHandler());
                    }
                });
    }


    ProxyConfig proxyConfig = SpringUtil.getBean(ProxyConfig.class);

    ProxyChannelManager proxyChannelManager = SpringUtil.getBean(ProxyChannelManager.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        logger.debug("ProxyMessage received {}", proxyMessage.getType());
        switch (proxyMessage.getType()) {
            case ProxyMessage.TYPE_HEARTBEAT:
                handleHeartbeatMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.C_TYPE_AUTH:
                handleAuthMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_CONNECT:
                handleConnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_DISCONNECT:
                handleDisconnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            default:
                break;
        }
    }

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel userChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (userChannel != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            buf.writeBytes(proxyMessage.getData());
            userChannel.writeAndFlush(buf);
        }

    }

    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = ctx.channel().attr(Constants.CLIENT_KEY).get();

        // 代理连接没有连上服务器有控制连接发送用户端断开连接消息
        if (clientKey == null) {
            String userId = proxyMessage.getUri();
            Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(ctx.channel(), userId);
            if (userChannel != null) {
                // 数据发送完成后，在关闭连接，解决http1.0数据传输问题
                userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }
        Channel cmdChannel = proxyChannelManager.getCmdChannel(clientKey);
        if (cmdChannel == null) {
            logger.warn("ConnectMessage:error cmd channel key {}", ctx.channel());
            return;
        }

        Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(cmdChannel, ctx.channel().attr(Constants.USER_ID).get());
        if (userChannel != null) {
            // 数据发送完成后在关闭连接，解决http1.0出数据传输问题
            userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            ctx.channel().attr(Constants.NEXT_CHANNEL).remove();
            ctx.channel().attr(Constants.CLIENT_KEY).remove();
            ctx.channel().attr(Constants.USER_ID).remove();

        }


    }

    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String uri = proxyMessage.getUri();
        if (uri == null) {
            ctx.channel().close();
            logger.warn("ConnectMessage:null uri");
            return;
        }
        String[] tokens = uri.split("@");
        if (tokens.length != 2) {
            ctx.channel().close();
            logger.warn("ConnectMessage:error uri");
            return;
        }
        Channel cmdChannel = proxyChannelManager.getCmdChannel(tokens[1]);
        if (cmdChannel == null) {
            ctx.channel().close();
            logger.warn("ConnectMessage:error cmd channel key {}", tokens[1]);
            return;
        }
        Channel userChannel = ProxyChannelManager.getUserChannel(cmdChannel, tokens[0]);
        if (userChannel != null) {
            ctx.channel().attr(Constants.USER_ID).set(tokens[0]);
            ctx.channel().attr(Constants.CLIENT_KEY).set(tokens[1]);
            ctx.channel().attr(Constants.NEXT_CHANNEL).set(userChannel);
            userChannel.attr(Constants.NEXT_CHANNEL).set(ctx.channel());
            // 代理客户端于后端服务器连接成功，修改用户连接为可读状态
            userChannel.config().setOption(ChannelOption.AUTO_READ, true);

        }

    }

    private void handleAuthMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = proxyMessage.getUri();
        Integer port = proxyConfig.getClientInetPorts(clientKey);
        if (port == null) {
            logger.info("error clientKey {}，{}", clientKey, ctx.channel());
            ctx.channel().close();
            return;
        }
        Channel channel = proxyChannelManager.getCmdChannel(clientKey);
        if (channel != null) {
            logger.warn("exist channel for key {}，{}", clientKey, ctx.channel());
            ctx.channel().close();
            return;
        }
        logger.info("set port ==> channel ,{},{},{}", clientKey, port, ctx.channel());
        proxyChannelManager.addCmdChannel(port, clientKey, ctx.channel());

        // 添加用户端的监听端口
        startUserPort(port);
    }

    private void handleHeartbeatMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        ProxyMessage heartbeatMessage = new ProxyMessage();
        heartbeatMessage.setSerialNumber(heartbeatMessage.getSerialNumber());
        heartbeatMessage.setType(ProxyMessage.TYPE_HEARTBEAT);
        logger.debug("response heartbeat message {}", ctx.channel());
        ctx.channel().writeAndFlush(heartbeatMessage);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel userChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (userChannel != null) {
            userChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 客户端断开连接时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Channel userChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        // 更新用户状态
        Integer portFromChannel = proxyChannelManager.getPortFromChannel(ctx.channel());
        if (portFromChannel != null)
            proxyConfig.updatePortStatus(portFromChannel, 0);
        if (userChannel != null && userChannel.isActive()) {
            String clientKey = ctx.channel().attr(Constants.CLIENT_KEY).get();
            String userId = ctx.channel().attr(Constants.USER_ID).get();
            Channel cmdChannel = proxyChannelManager.getCmdChannel(clientKey);
            if (cmdChannel == null) {
                ProxyChannelManager.removeUserChannelFromCmdChannel(cmdChannel, userId);
            } else {
                logger.warn("null cmdChannel,clientKey is {}", clientKey);
            }
            // 数据发送完成后关闭连接，解决http1.0数据传输问题
            userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            userChannel.close();
        } else {
            ProxyChannelManager.removeCmdChannel(ctx.channel());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 监听用户端口和更改数据可连接状态为1
     *
     * @param port
     */
    private void startUserPort(Integer port) {
        try {
            bootstrap.bind(port).get();
            proxyConfig.updatePortStatus(port, 1);
            logger.info("bind user port" + port);
        } catch (Exception ex) {
            // BindException表示该端口已经绑定过
            if (!(ex.getCause() instanceof BindException)) {
                throw new RuntimeException(ex);
            }
        }
    }

}
