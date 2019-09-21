package com.xujian.netty.demo.webSocketChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
 * description
 * <pre>
 *  TODO  websocket 聊天服务器
 *  </pre>
 * @return
 * @date 2019/9/21 15:49
 * @author xujianghrx@gmail.com
 */
public class WebsocketChatServer {
  private int port;

  public WebsocketChatServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
    int port;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    } else {
      port = 8080;
    }
    new WebsocketChatServer(port).run();

  }

  private void run() throws InterruptedException {

    EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap(); // (2)
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class) // (3)
          .childHandler(new WebsocketChatServerInitializer())  //(4)
          .option(ChannelOption.SO_BACKLOG, 128)          // (5)
          .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

      System.out.println("WebsocketChatServer 启动了" + port);

      // 绑定端口，开始接收进来的连接
      ChannelFuture f = b.bind(port).sync(); // (7)

      // 等待服务器  socket 关闭 。
      // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
      f.channel().closeFuture().sync();

    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();

      System.out.println("WebsocketChatServer 关闭了");
    }
  }

}
