package com.xujian.netty.demo.simpleChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
 * description
 * <pre>
 *
 *  TODO 简单聊天服务器
 *
 * </pre>
 * @return
 * @date 2019/9/21 15:01
 * @author xujianghrx@gmail.com
 */
public class SimpleChatServer {
  private int port;

  public SimpleChatServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) {
    int port;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    } else {
      port = 8080;
    }
    new SimpleChatServer(port).run();
  }

  /**
   * 服务器启动
   */
  private void run() {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    ServerBootstrap serverBootstrap = new ServerBootstrap();

    serverBootstrap
        .group(bossGroup, workerGroup)//主从模型
        .channel(NioServerSocketChannel.class)//指定信道类型
        .childHandler(new SimpleChatServerInitializer())//指定处理请求的处理器
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);

    System.out.println("SimpleChatServer 启动了");

    // 绑定端口，开始接收进来的连接
    try {
      ChannelFuture future = serverBootstrap.bind(port).sync();

      // 等待服务器  socket 关闭 。
      // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();

      System.out.println("SimpleChatServer 关闭了");
    }

  }

}
