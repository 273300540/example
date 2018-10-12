package com.xlm.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {
    static AtomicInteger integer = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0;i<1000;i++){
            integer.set(0);
            System.out.println("执行"+i);
            main1(args);
        }
    }
    public static void main1(String[] args) throws InterruptedException {
        final String host = "mvnrepository.com";
        final String path = "";
        final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ChannelHandler clientCodec = new MyChannelHandle();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).handler(clientCodec).channelFactory(new ReflectiveChannelFactory(NioSocketChannel.class));
        final ChannelFuture connect = bootstrap.connect(host, 80);
        /*connect.addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://"+host+path);
                httpRequest.headers().set(HttpHeaderNames.HOST, host);
                httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                //httpRequest.headers().set(HttpHeaders.Names.CONTENT_LENGTH, httpRequest.content().readableBytes());//可以在httpRequest.headers中设置各种需要的信息。

                final ChannelFuture writeFuture = connect.channel().writeAndFlush(httpRequest);
                //writeFuture.sync();
                writeFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        ChannelFuture closeFuture = writeFuture.channel().closeFuture();
                        closeFuture.sync();
                        eventLoopGroup.shutdownGracefully();
                    }
                });
            }
        });*/

        final Channel channel = connect.sync().channel();
        DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://" + host + path);
        httpRequest.headers().set(HttpHeaderNames.HOST, host);
        httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        final ChannelFuture writeFuture = connect.channel().writeAndFlush(httpRequest);
        integer.incrementAndGet();
        writeFuture.sync();
        ChannelFuture closeFuture = writeFuture.channel().closeFuture();
        closeFuture.sync();
        eventLoopGroup.shutdownGracefully();
    }

    public static class MyChannelHandle extends ChannelInboundHandlerAdapter {
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            ctx.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    if (integer.get() == 0) {
                        System.out.println("==================================================================");
                    }
                    super.channelRead(ctx,msg);
                }
            }).addLast(new HttpClientCodec()).addLast(new ChannelInboundHandlerAdapter() {
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                   // System.out.println(msg);

                    super.channelRead(ctx, msg);
                }
            });
            super.channelRegistered(ctx);
        }
       /* public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.pipeline().channel().
            super.channelActive(ctx);
        }*/
    }

}
