package com.atguigu.eduservice.netty;


import com.alibaba.fastjson.JSON;
import com.atguigu.eduservice.entity.Message;
import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.service.ChatRecordService;
import com.atguigu.eduservice.utils.IdWorker;
import com.atguigu.eduservice.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.util.StringUtils;

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

    //当Channel中有新的事件消息后会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //  当接收到数据后自动调用

        //  获取客户端发过来的文本消息
        String text = msg.text();
        System.out.println("接受到消息数据为："+text);

        UserChannelMap.print();

        //通过SpringUtil工具类获取Spring上下文容器
        ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);

        Message message = JSON.parseObject(text, Message.class);
        switch (message.getType()){
            //处理客户端连接的消息
            case 0:
                //建立用户与通道的关联
                String userId = message.getTbChatRecord().getUserId();
                UserChannelMap.put(userId,ctx.channel());
                System.out.println("建立用户:"+userId+"与通道"+ctx.channel().id()+"的关联");
                break;
            //处理客户端发送好友消息
            case 1:
                //将聊天消息保存到数据库
                TbChatRecord chatRecord = message.getTbChatRecord();

                chatRecordService.insert(chatRecord);
                //如果发送消息好友在线，可以直接将消息发送给好友
                Channel channel=UserChannelMap.get(chatRecord.getFriendId());
                if(!StringUtils.isEmpty(channel)){
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }else {
                    //如果不在线，暂时不发送
                    System.out.println("用户:"+chatRecord.getFriendId()+"不在线");
                }
                break;
        }
    }


    //  当有新的客户端连接到服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //   将新的通道加入到clients
        clients.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭通道");
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        UserChannelMap.print();
    }
}
