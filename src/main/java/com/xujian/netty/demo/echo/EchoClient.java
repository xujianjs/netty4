package com.xujian.netty.demo.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/*
 * description
 *
 * <pre>
 *  TODO  1.创建 Bootstrap
 *  TODO  2.指定 EventLoopGroup 来处理客户端事件。由于我们使用 NIO 传输，所以用到了 NioEventLoopGroup 的实现
 *  TODO  3.使用的 channel 类型是一个用于 NIO 传输的信道
 *  TODO  4.设置服务器的 InetSocketAddress
 *  TODO  5.当建立一个连接和一个新的通道时，创建添加到 EchoClientHandler 实例 到 channel pipeline
 *  TODO  6.连接到远程;调用sync()阻塞主线程  等待连接完成
 *  TODO  7.阻塞直到 Channel 关闭
 *  TODO  8..调用 shutdownGracefully() 来关闭线程池和释放所有资源
 *</pre>
 *
 *
 * @date 2019/9/19 22:28
 * @author xujianghrx@gmail.com
 */
public class EchoClient {

  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap(); //1
      b.group(group) //2
          .channel(NioSocketChannel.class) //3
          .remoteAddress(new InetSocketAddress(host, port)) //4
          .handler(new ChannelInitializer<SocketChannel>() { //5
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new EchoClientHandler());
            }
          });
      ChannelFuture f = b.connect().sync(); //6
      f.channel().closeFuture().sync(); //7
    } finally {
      group.shutdownGracefully().sync(); //8
    }
  }

  public static void main(String[] args) throws Exception {
    //args参数可以在run configurations 中设置：program arguments 的一个参数 【以空格隔开】
    if (args.length != 2) {
      System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
      return;
    }
    final String host = args[0];
    final int port = Integer.parseInt(args[1]);
    new EchoClient(host, port).start();
  }
}