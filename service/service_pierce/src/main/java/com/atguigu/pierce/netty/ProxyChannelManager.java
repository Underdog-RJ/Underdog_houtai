package com.atguigu.pierce.netty;

import com.atguigu.pierce.entity.ClientMQ;
import com.atguigu.pierce.entity.ConfigChangedListener;
import com.atguigu.pierce.netty.config.ProxyConfig;
import com.atguigu.pierce.netty.protocol.Constants;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 代理服务连接管理(代理客户端连接+用户请求连接)
 */
@Component
@RabbitListener(queues = "pierce.changeInfo")
public class ProxyChannelManager {

    private static Logger logger = LoggerFactory.getLogger(ProxyChannelManager.class);

    @Autowired
    ProxyConfig proxyConfig;

    private static final AttributeKey<Map<String, Channel>> USER_CHANNELS = AttributeKey.newInstance("user_channels");

    /**
     * 代理信息
     */
    private static final AttributeKey<String> REQUEST_LAN_INFO = AttributeKey.newInstance("request_lan_info");

    /**
     * 端口号列表
     */
    private static final AttributeKey<Integer> CHANNEL_PORT = AttributeKey.newInstance("channel_port");

    /**
     * 用户的clientKey
     */
    private static final AttributeKey<String> CHANNEL_CLIENT_KEY = AttributeKey.newInstance("channel_client_key");

    /**
     * 端口和channel
     */
    private static Map<Integer, Channel> portCmdChannelMapping = new ConcurrentHashMap<Integer, Channel>();

    /**
     * clientkey和channel
     * 一个key可以申请多个channel
     */
    private static Map<String, Channel> cmdChannels = new ConcurrentHashMap<String, Channel>();

    /**
     * 用户更换clientKey或者lan
     */
    @RabbitHandler
    public void changeInfo(ClientMQ clientMQ) {
        // 判断是clientKey还是lanInfo
        // clientKey
        String clientKeyBefore = clientMQ.getClientKeyBefore();
        // 从clientKey中获取当前激活的channel
        // 判断当前的clientKey是否是激活状态
        // 如果当前是激活状态
        if (cmdChannels.containsKey(clientKeyBefore)) {
            // 进行当前通道
            Channel channel = cmdChannels.get(clientKeyBefore);
            removeCmdChannel(channel);
        }
//        if (clientMQ.getType().equals(0)) {
//            String clientKeyBefore = clientMQ.getClientKeyBefore();
//            // 从clientKey中获取当前激活的channel
//            // 判断当前的clientKey是否是激活状态
//            // 如果当前是激活状态
//            if (cmdChannels.containsKey(clientKeyBefore)) {
//                // 进行当前通道
//                Channel channel = cmdChannels.get(clientKeyBefore);
//                if (channel.isActive()) {
//                    removeCmdChannel();
//                }
//            }
//        }
    }
//    static {
//        proxyConfig.addConfigChangedListener(new ConfigChangedListener() {
//            /**
//             * 代理配置发生变化时回调
//             */
//            @Override
//            public void onChanged() {
//                Iterator<Map.Entry<String, Channel>> ite = cmdChannels.entrySet().iterator();
//                while (ite.hasNext()) {
//                    Channel proxyChannel = ite.next().getValue();
//                    String clientKey = proxyChannel.attr(CHANNEL_CLIENT_KEY).get();
//
//                    // 去除已经去掉的clientKey配置
//                    Set<String> clientKeySet = proxyConfig.getClientKeySet();
//                    if (!clientKeySet.contains(clientKey)) {
//                        removeCmdChannel(proxyChannel);
//                        continue;
//                    }
//                    if (proxyChannel.isActive()) {
//                        List<Integer> inetPorts = new ArrayList<Integer>(proxyConfig.getClientInetPorts(clientKey));
//                        Set<Integer> inetPortSet = new HashSet<Integer>(inetPorts);
//                        List<Integer> channelInetPorts = new ArrayList<Integer>(proxyChannel.attr(CHANNEL_PORT).get());
//
//                        synchronized (portCmdChannelMapping) {
//                            // 移除旧的映射关系
//                            for (Integer channelInetPort : channelInetPorts) {
//                                Channel channel = portCmdChannelMapping.get(channelInetPort);
//                                if (channel == null) {
//                                    continue;
//                                }
//                                // 判断是否用同一个连接对象，有可能之前已经更换其他的client的连接
//                                if (proxyChannel == channel) {
//                                    if (!inetPortSet.contains(channelInetPort)) {
//                                        portCmdChannelMapping.remove(channelInetPort);
//                                        proxyChannel.attr(CHANNEL_PORT).remove();
//                                    } else {
//                                        // 端口已经在该proxyChannel中使用了
//                                        inetPorts.remove(channelInetPort);
//                                    }
//                                }
//                            }
//
//                            // 讲新配置中增加的外网端口写到映射配置中
//                            for (Integer inetPort : inetPorts) {
//                                portCmdChannelMapping.put(inetPort, proxyChannel);
//                                proxyChannel.attr(CHANNEL_PORT).set(inetPort);
//                            }
//                            checkAndClearUserChannel(proxyChannel);
//                        }
//                    }
//                }
//                Iterator<Map.Entry<String, Channel>> iterator = cmdChannels.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, Channel> entry = iterator.next();
//                    Channel proxyChannel = entry.getValue();
//                    logger.info("proxyChannel config, {}, {}, {} ,{}", entry.getKey(), proxyChannel, getUserChannels(proxyChannel).size(), proxyChannel.attr(CHANNEL_PORT).get());
//
//                }
//            }
//
//            /**
//             * 检测连接配置是否与当前配置一致，不一致则关闭
//             */
//            private void checkAndClearUserChannel(Channel proxyChannel) {
//                Map<String, Channel> userChannels = getUserChannels(proxyChannel);
//                Iterator<Map.Entry<String, Channel>> userChannelIte = userChannels.entrySet().iterator();
//                while (userChannelIte.hasNext()) {
//                    Map.Entry<String, Channel> entry = userChannelIte.next();
//                    Channel userChannel = entry.getValue();
//                    String requestLanInfo = getUserChannelRequestLanInfo(userChannel);
//                    InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
//                    String lanInfo = proxyConfig.getLanInfo(sa.getPort());
//
//                    // 判断当前配置中对应外网端口的lan信息是否与正在运行的连接中的lan信息是否一致
//                    if (lanInfo == null || !lanInfo.equals(requestLanInfo)) {
//                        userChannel.close();
//                        userChannels.remove(entry.getKey());
//                    }
//                }
//            }
//        });


//    }

    /**
     * 增加代理服务器端口代理控制端连接的映射关系
     *
     * @param port
     * @param clientKey
     * @param channel
     */
    public void addCmdChannel(Integer port, String clientKey, Channel channel) {
        if (port == null)
            throw new IllegalArgumentException("port can not be null");
        // 客户端(proxy-client),这里同步的比较重
        // 保证服务器对外端口与客户端到服务器的连接关系在临界情况下调用removeChannel不出现问题
        portCmdChannelMapping.put(port, channel);
        channel.attr(CHANNEL_PORT).set(port);
        channel.attr(CHANNEL_CLIENT_KEY).set(clientKey);
        channel.attr(USER_CHANNELS).set(new ConcurrentHashMap<>());
        cmdChannels.put(clientKey, channel);
    }

    /**
     * 通过channel获取port
     *
     * @param channel
     */
    public Integer getPortFromChannel(Channel channel) {
        Integer integer = channel.attr(CHANNEL_PORT).get();
        return integer;
    }

    public static void removeCmdChannel(Channel channel) {
        logger.warn("channel closed,clear user channels,{}", channel);
        if (channel.attr(CHANNEL_PORT).get() == null) {
            return;
        }
        String clientKey = channel.attr(CHANNEL_CLIENT_KEY).get();
        Channel channel0 = cmdChannels.remove(clientKey);
        if (channel != channel0) {
            cmdChannels.put(clientKey, channel);
        }

        Integer port = channel.attr(CHANNEL_PORT).get();
        Channel proxyChannel = portCmdChannelMapping.remove(port);
        // 在执行断开之前新的连接已经连上来了
        if (proxyChannel != channel)
            portCmdChannelMapping.put(port, proxyChannel);

        if (channel.isActive()) {
            logger.info("disconnect proxy channel {}", channel);
            channel.close();
        }
        Map<String, Channel> userChannels = getUserChannels(channel);
        Iterator<String> iterator = userChannels.keySet().iterator();
        while (iterator.hasNext()) {
            Channel userChannel = userChannels.get(iterator.next());
            if (userChannel.isActive()) {
                userChannel.close();
                logger.info("disconnect user channel {}", userChannel);
            }
        }
    }

    public Channel getCmdChannel(Integer port) {
        return portCmdChannelMapping.get(port);
    }

    public Channel getCmdChannel(String clientKey) {
        return cmdChannels.get(clientKey);
    }


    /**
     * 增加用户连接于代理客户端连接关系
     *
     * @param cmdChannel
     * @param userId
     * @param userChannel
     */
    public void addUserChannelToCmdChannel(Channel cmdChannel, String userId, Channel userChannel) {
        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        String lanInfo = proxyConfig.getLanInfo(sa.getPort());
        userChannel.attr(Constants.USER_ID).set(userId);
        userChannel.attr(REQUEST_LAN_INFO).set(lanInfo);
        cmdChannel.attr(USER_CHANNELS).get().put(userId, userChannel);
    }

    /**
     * 删除用户连接于代理客户端连接关系
     */
    public static Channel removeUserChannelFromCmdChannel(Channel cmdChannel, String userId) {
        if (cmdChannel.attr(USER_CHANNELS).get() == null) {
            return null;
        }
        synchronized (cmdChannel) {
            return cmdChannel.attr(USER_CHANNELS).get().remove(userId);
        }
    }

    /**
     * 根据代理客户端连接于用户编号获取用户连接
     *
     * @param cmdChannel
     * @param userId
     * @return
     */
    public static Channel getUserChannel(Channel cmdChannel, String userId) {
        return cmdChannel.attr(USER_CHANNELS).get().get(userId);
    }


    /**
     * 获取用户编号
     *
     * @param userChannel
     * @return
     */
    public static String getUserChannelUserId(Channel userChannel) {
        return userChannel.attr(Constants.USER_ID).get();
    }

    /**
     * 获取用户请求的内网IP端口信息
     *
     * @param userChannel
     * @return
     */
    public static String getUserChannelRequestLanInfo(Channel userChannel) {
        return userChannel.attr(REQUEST_LAN_INFO).get();
    }


    /**
     * 获取代理控制端连接绑定的所有用户连接
     *
     * @param cmdChannel
     * @return
     */
    public static Map<String, Channel> getUserChannels(Channel cmdChannel) {
        return cmdChannel.attr(USER_CHANNELS).get();
    }

}
