package com.atguigu.eduservice.thread;

import java.util.concurrent.*;

public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*CompletableFuture.runAsync(()->{
            System.out.println("当前线程:"+Thread.currentThread().getId());
            int i=10 /2;
            System.out.println("运行结果:"+i);
        },executor);*/

        /**
         * 方法完成后的感知
         */
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果:" + i);
            return i;
        }, executor).whenComplete((res,exception)->{
            //虽然得到异常信息，但是没法修改返回数据
            System.out.println("异步任务完成了...结果是："+res+";异常是:"+exception);
        }).exceptionally(throwable -> {
            //可以感知到异常，但是返回默认值
            return 10;
        });*/

        /**
         * 方法执行完成后的处理
         */
       /* CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 4;
            System.out.println("运行结果:" + i);
            return i;
        }, executor).handle((res,thr)->{
            //正常处理逻辑
           if(res!=null){
               return res*2;
           }
           //异常处理逻辑
           if(thr!=null){
               return 1;
           }
           //两个都不走的处理逻辑
           return 0;
        });*/

        /**
         * 线程串行话
         *  有无Async的区别在于额外开辟一个线程池
         *  1.thenRunAsync:不能获取上一步的执行结果
         *       .thenRunAsync(()->{
         *             System.out.println("任务2启动了");
         *         },executor);
         *  2.thenAccept能接收上一步结果，但是无返回值
         *      .thenAcceptAsync((res)->{
         *             System.out.println("任务2启动了..."+res);
         *         });
         * 3.thenApplyAsync能接收上一步的结果，且有返回值
         *      .thenApplyAsync((res)->{
         *             System.out.println("任务2启动了..."+res);
         *             return "Hello"+res;
         *         },executor);
         */
     /*  CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 4;
            System.out.println("运行结果:" + i);
            return i;
        }, executor)*/



        /**
         * 两个都完成
         *      1.runAfterBothAsync 不能感知到前两个的返回值
         *             future01.runAfterBothAsync(future02,()->{
         *             System.out.println("任务3开始");
         *              },executor);
         *      2.thenAcceptBothAsync能感知到前两个的执行结果
         *              future01.thenAcceptBothAsync(future02,(f1,f2)->{
         *               System.out.println("任务3开始,之前的结果是:"+f1+"-->"+f2);
         *              },executor)
         *      3.thenCombineAsync能感知到前两个的执行结果，且有返回值
         *               CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
         *             return f1 + ":" + f2 + "-》Hello";
         *             }, executor);
         *
         *
         */
      /*  CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {

            System.out.println("当前线程1:" + Thread.currentThread().getId());
            int i = 10 / 4;
            System.out.println("运行结果:" + i);
            return i;
        }, executor);

        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程2:" + Thread.currentThread().getId());
            return "Hello";
        }, executor);

        future01.runAfterBothAsync(future02,()->{
            System.out.println("任务3开始");
        },executor);
        future01.thenAcceptBothAsync(future02,(f1,f2)->{
            System.out.println("任务3开始,之前的结果是:"+f1+"-->"+f2);
        },executor);
        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
            return f1 + ":" + f2 + "-》Hello";
        }, executor);*/

        /**
         * 两个任务只要有一个完成，我们就执行任务3
         *      1.runAfterEitherAsync不感知返回值，且自己无返回值
         *      2.acceptEitherAsync感知返回结果但是两个futue的返回结果应该保持一直，但无返回值
         *      3.applyToEitherAsync感知结果且有返回
         */


       /* CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
            return 1;
        }, executor);
        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        });*/
//        future01.runAfterEitherAsync(future02,()->{
//            System.out.println("任务三开始了");
//        },executor);

//        future01.acceptEitherAsync(future02,(res)->{
//            System.out.println(res);
//        },executor);

//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> res.toString() + "啊哈", executor);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> "img", executor);
        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> "attr", executor);
        CompletableFuture<String> futureDesc= CompletableFuture.supplyAsync(() -> "desc", executor);

        //将三个任务编排到一起 allOf全部完成
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        //此时必须调用allOf.get()或者allOf.join()
//        allOf.get();
//        System.out.println("main...end..."+futureImg.get() + futureAttr.get()+futureDesc.get());

        //anyOf任意一个完成
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get();
        System.out.println(anyOf.get());

    }
}
