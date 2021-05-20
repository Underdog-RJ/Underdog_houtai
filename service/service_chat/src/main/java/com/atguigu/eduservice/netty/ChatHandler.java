package com.atguigu.eduservice.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //  用来保留所有的客户端连接
    private static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:MM");

    //当Channel中有新的事件消息后会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //  当接收到数据后自动调用

        //  获取客户端发过来的文本消息
        String text = msg.text();
        System.out.println("接受到消息数据为："+text);

        Message message = JSON.parseObject(text, Message.class);
        switch (message.getType()){
            case "1":
                //建立用户与通道的关联
                String userId = message.getTbChatRecord().getUserId();
                UserChannelMap.put(userId,ctx.channel());
                System.out.println("建立用户:"+userId+"与通道"+ctx.channel().id()+"的关联");

                break;
        }
    }


    //  当有新的客户端连接到服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //   将新的通道加入到clients
        clients.add(ctx.channel());
    }
}
