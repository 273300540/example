package com.xlm.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ServerApp {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(nioEventLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new ChannelInboundHandlerAdapter() {
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("===========新的连接==============="+msg);
                super.channelRead(ctx,msg);
            }
        });
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                System.out.println(ch);
                ch.pipeline().addLast(new HttpServerCodec()).addLast(new HttpObjectAggregator(Integer.MAX_VALUE)).
                        addLast(new StringChannelHandle());
            }
        });
        ChannelFuture future = serverBootstrap.bind(8080).sync();
        while(true){
            Thread.currentThread().sleep(100000L);
        }


    }
    public static class StringChannelHandle extends ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof App2.HttpResponseCollector) {
                App2.HttpResponseCollector collector = ((App2.HttpResponseCollector) msg);
                System.out.println(collector.headToString());
                System.out.println(collector.extractCharset());
                System.out.println(collector.bodyToString("utf-8"));
            } else if (msg instanceof FullHttpMessage) {
                FullHttpMessage fullHttpMessage = (FullHttpMessage)msg;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                fullHttpMessage.content().readBytes(byteArrayOutputStream,fullHttpMessage.content().readableBytes());
                System.out.println( new String(byteArrayOutputStream.toByteArray()));
                byte[] bytes = "[{\"login\":\"xlm\"}]".getBytes();
                ByteBuf buffer = ctx.channel().alloc().ioBuffer(bytes.length);
                buffer.writeBytes(bytes);
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,buffer);
                response.headers().add(HttpHeaderNames.CONTENT_LENGTH,bytes.length);
                ctx.writeAndFlush(response);
            }
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
            ctx.fireExceptionCaught(cause);
        }
    }
}
