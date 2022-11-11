package com.atguigu.eduservice.controller;

import com.atguigu.eduservice.entity.ImageRecoGrpc;
import com.atguigu.eduservice.entity.ImageRecoOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class test {
    private final ManagedChannel channel;
    private final ImageRecoGrpc.ImageRecoBlockingStub blockingStub;
    private static final String GRPC_SERVER_HOST = "127.0.0.1";
    private static final int GRPC_SERVER_PORT = 17890;

    public test(String host, int port) {
        // usePlaintext表示明文传输，否则需要配置ssl, channel  表示通信通道
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        // 存根，用于调用服务端的接口方法。
        blockingStub = ImageRecoGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void testResult() {
        String allPath = "F:\\image\\1-0.jpg";
        ImageRecoOuterClass.ImageDto build = ImageRecoOuterClass.ImageDto.newBuilder().setImagePath(allPath).build();
        ImageRecoOuterClass.ResImageDto response = blockingStub.getAllImageFromAI(build);
        System.out.println(response);
    }

    public static void main(String[] args) {
        test client = new test(GRPC_SERVER_HOST, GRPC_SERVER_PORT);
        client.testResult();
    }


}
