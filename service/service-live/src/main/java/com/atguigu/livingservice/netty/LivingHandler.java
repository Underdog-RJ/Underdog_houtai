package com.atguigu.livingservice.netty;

import com.alibaba.fastjson.JSON;
import com.atguigu.livingservice.entity.MessageType;
import com.atguigu.livingservice.service.EduLivingService;
import com.atguigu.livingservice.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = "living.delay.living.queue")
public class LivingHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //  用来保留所有的客户端连接
    private static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //
    private static Map<String,List<Channel>> map=new HashMap<>();

    @RabbitHandler
    public void removeLiving(String msg){
        if(map.containsKey(msg)){
            map.remove(msg);
            System.out.println("清除直播:"+msg);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //  获取客户端发过来的文本消息
        String text = msg.text();
        System.out.println("接受到消息数据为："+text);
        MessageType message = JSON.parseObject(text, MessageType.class);
        String livingId = message.getLivingId();
        EduLivingService livingService = SpringUtil.getBean(EduLivingService.class);
        switch (message.getType()){
            //处理客户端连接的消息
            case 0:
                //建立与通道的关联
                if(map.containsKey(livingId)){
                    List<Channel> channels = map.get(livingId);
                    channels.add(ctx.channel());
                    message.setCount(channels.size());
                    for (Channel channel : channels) {
                        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                }else {
                    List<Channel> list=new ArrayList<>();
                    list.add(ctx.channel());
                    message.setCount(1);
                    map.put(livingId,list);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
                break;
            case 1:
                if(map.containsKey(livingId)){
                    livingService.insert(message);
                    List<Channel> list = map.get(livingId);
                    message.setCount(list.size());
                    for (Channel channel : list) {
                        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                }

        }
    }

    //  当有新的客户端连接到服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //   将新的通道加入到clients

        clients.add(ctx.channel());
    }
}
