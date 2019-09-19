package com.xujian.netty.demo.echo;
import	java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 徐健
 * @version 1.0.0
 * @ClassName EchoServer.java
 * @Description
 * TODO 服务器引导程序
 * TODO 监听和接收进来的连接请求
 * TODO 配置 Channel 来通知一个关于入站消息的 EchoServerHandler 实例
 * TODO
 * @createTime 2019年09月19日 17:31:00
 *
 *
 *
 * 1.设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确）
 * 2.呼叫服务器的 start() 方法
 * 3.创建 EventLoopGroup
 * 4.创建 ServerBootstrap
 * 5.指定使用 NIO 的传输 Channel
 * 6.设置 socket 地址使用所选的端口
 * 7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline
 * 8.绑定的服务器;sync 等待服务器关闭
 * 9.关闭 channel 和 块，直到它被关闭
 * 10.关机的 EventLoopGroup，释放所有资源。
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);        //1
        new EchoServer(port).start();
    }
}
