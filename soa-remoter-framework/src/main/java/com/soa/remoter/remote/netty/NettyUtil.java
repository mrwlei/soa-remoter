package com.soa.remoter.remote.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty通讯的工具类
 */
public class NettyUtil {
    public static void startServer(String port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new NettyServerInHandler());
                        }
                        
                    })
                    .option(ChannelOption.SO_BACKLOG, 128);
            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    public static String sendMsg(String host, String port, final String sendmsg)
            throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final StringBuffer resultmsg = new StringBuffer();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyClientInHandler(resultmsg,
                            sendmsg));
                }
                
            });
            //连接服务端，一直在等待着服务端的返回消息，返回的信息封装到future，可以监控线程的返回
            ChannelFuture f = b.connect(host, Integer.parseInt(port))
                    .channel()
                    .closeFuture()
                    .await();
            return resultmsg.toString();
        }
        finally {
            workerGroup.shutdownGracefully();
        }
    }
}
