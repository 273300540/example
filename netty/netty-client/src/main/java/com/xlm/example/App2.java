package com.xlm.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class App2 {
    public static void main(String[] args) throws InterruptedException {
        final String host = "mvnrepository.com";
        final String path = "";
        final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ChannelHandler clientCodec = new MyChannelHandle();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).handler(clientCodec).channelFactory(new ChannelFactory<Channel>() {
            @Override
            public Channel newChannel() {
                NioSocketChannel socketChannel = new NioSocketChannel();
                //socketChannel.pipeline().addLast(new HttpClientCodec()).addLast(new MyChannelHandle()).addLast(new StringChannelHandle());
                socketChannel.pipeline().addLast(new HttpClientCodec()).addLast(new HttpObjectAggregator(Integer.MAX_VALUE)).addLast(new StringChannelHandle());
                return socketChannel;
            }
        });

        final ChannelFuture connect = bootstrap.connect(host, 80);
        final Channel channel = connect.sync().channel();
        DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://" + host + path);
        httpRequest.headers().set(HttpHeaderNames.HOST, host);
        httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        final ChannelFuture writeFuture = connect.channel().writeAndFlush(httpRequest);
        writeFuture.sync();
        ChannelFuture closeFuture = writeFuture.channel().closeFuture();
        closeFuture.sync();
        eventLoopGroup.shutdownGracefully();
    }

    public static class MyChannelHandle extends ChannelInboundHandlerAdapter {
        HttpResponseCollector collector = new HttpResponseCollector();

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (collector.addMsg(msg)) {
                super.channelRead(ctx, collector);
            }
        }
    }

    public static class StringChannelHandle extends ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpResponseCollector) {
                HttpResponseCollector collector = ((HttpResponseCollector) msg);
                System.out.println(collector.headToString());
                System.out.println(collector.extractCharset());
                System.out.println(collector.bodyToString("utf-8"));
            } else if (msg instanceof FullHttpMessage) {
                FullHttpMessage fullHttpMessage = (FullHttpMessage)msg;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                fullHttpMessage.content().readBytes(byteArrayOutputStream,fullHttpMessage.content().readableBytes());
                System.out.println( new String(byteArrayOutputStream.toByteArray()));
            }
        }
    }

       /* public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.pipeline().channel().
            super.channelActive(ctx);
        }*/

    public static class HttpResponseCollector {
        private DefaultHttpResponse httpResponse;
        private List<HttpContent> httpContentList;
        private boolean result = false;
        private String charset;

        public boolean addMsg(Object msg) {
            if (result) {
                reset();
            }
            if (msg instanceof DefaultHttpContent) {
                if (httpContentList == null) {
                    httpContentList = new ArrayList<HttpContent>();
                }
                httpContentList.add((DefaultHttpContent) msg);
                if (msg instanceof DefaultLastHttpContent) {
                    result = true;
                }
            } else if (msg instanceof DefaultHttpResponse) {
                httpResponse = (DefaultHttpResponse) msg;
            }
            return result;
        }

        public void reset() {
            httpResponse = null;
            if (httpContentList != null) {
                for (HttpContent content : httpContentList) {
                    content.content().release();
                }
                httpContentList.clear();
            }
            result = false;
            charset = null;
        }

        public String headToString() {
            return httpResponse.toString();
        }

        public String bodyToString(String charset) throws IOException {
            if (httpContentList != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                for (HttpContent one : httpContentList) {
                    one.content().readBytes(byteArrayOutputStream, one.content().readableBytes());
                }
                String tempCharset = extractCharset();
                if (tempCharset != null) {

                    return new String(byteArrayOutputStream.toByteArray(), tempCharset);
                }
                return new String(byteArrayOutputStream.toByteArray(), charset);
            }
            return null;
        }

        public String extractCharset() {
            if (charset != null) {
                return charset;
            }
            String str = httpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE);
            if (str == null) {
                return null;
            }
            String[] values = str.split(";");
            for (String one : values) {
                String[] temp = one.split("=");
                if (temp.length < 2) {
                    continue;
                }
                if ("charset".equalsIgnoreCase(temp[0].trim())) {
                    charset = temp[1];
                }
            }

            return charset;
        }
    }
}
