package com.xujian.netty.demo.simpleChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * description
 * <pre>
 *
 * TODO 简单聊天客户端
 *
 * </pre>
 * @return
 * @date 2019/9/21 15:13
 * @author xujianghrx@gmail.com
 */
public class SimpleChatClient {
  private final String host;
  private final int port;

  public SimpleChatClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static void main(String[] args) throws Exception{
    new SimpleChatClient("localhost", 8080).run();
  }

  private void run() {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap  = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new SimpleChatClientInitializer());

      //连接聊天服务器 获取聊天信道
      Channel channel = bootstrap.connect(host, port).sync().channel();
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      for (; ; ) {
        //通过信道载体传输控制台的输入流
        channel.writeAndFlush(in.readLine() + "\r\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }
  }

}
