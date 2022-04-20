package com.atguigu.pierce.netty;

import com.atguigu.pierce.entity.ConfigChangedListener;
import com.atguigu.pierce.netty.config.PropertiesConfig;
import com.atguigu.pierce.netty.config.ProxyConfig;
import com.atguigu.pierce.netty.handlers.ServerChannelHandler;
import com.atguigu.pierce.netty.handlers.UserChannelHandler;
import com.atguigu.pierce.netty.metrics.handler.ByteMetricsHandler;
import com.atguigu.pierce.netty.protocol.IdleCheckHandler;
import com.atguigu.pierce.netty.protocol.ProxyMessageDecoder;
import com.atguigu.pierce.netty.protocol.ProxyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.BindException;
import java.util.List;

@Component
@EnableConfigurationProperties(PropertiesConfig.class)
public class ProxyServer implements ConfigChangedListener {

    @Autowired
    PropertiesConfig propertiesConfig;

    /**
     * max packet is 2M.
     */
    private static final int MAX_FRAME_LENGTH = 2 * 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    private static Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private NioEventLoopGroup serverWorkerGroup;

    private NioEventLoopGroup serverBossGroup;

    @Autowired
    ProxyConfig proxyConfig;

    public ProxyServer() {

        serverBossGroup = new NioEventLoopGroup();
        serverWorkerGroup = new NioEventLoopGroup();

//        ProxyConfig.getInstance().addConfigChangedListener(this);
    }

    @PostConstruct
    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                ch.pipeline().addLast(new ProxyMessageEncoder());
                ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME, 0));
                ch.pipeline().addLast(new ServerChannelHandler(serverBossGroup,serverWorkerGroup));
            }
        });
        try {
            bootstrap.bind(propertiesConfig.getServerBind(), propertiesConfig.getServerPort()).get();
            logger.info("proxy server start on port " + propertiesConfig.getServerPort());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
//        startUserPort();
    }

    @PreDestroy
    public void stop() {

        serverBossGroup.shutdownGracefully();
        serverWorkerGroup.shutdownGracefully();
    }

    private void startUserPort() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addFirst(new ByteMetricsHandler());
                        ch.pipeline().addLast(new UserChannelHandler());

                    }
                });
        List<Integer> ports = proxyConfig.getUserPorts();

        for (Integer port : ports) {
            try {
                bootstrap.bind(port).get();
                logger.info("bind user port" + port);
            } catch (Exception ex) {
                // BindException表示该端口已经绑定过
                if (!(ex.getCause() instanceof BindException)) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void onChanged() {
        startUserPort();
    }
}
