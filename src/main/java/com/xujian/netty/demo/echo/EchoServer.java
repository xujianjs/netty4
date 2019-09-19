package com.xujian.netty.demo.echo;

import io.netty.channel.ChannelHandler.Sharable;
import java.net.InetSocketAddress;

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
 * @Description <pre>
 * TODO 服务器引导程序
 * TODO 监听和接收进来的连接请求
 * TODO 配置 Channel 来通知一个关于入站消息的 EchoServerHandler 实例
 * </pre>
 * @createTime 2019年09月19日 17:31:00
 *
 *
 * <pre>
 * TODO 1.设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确）
 * TODO 2.呼叫服务器的 start() 方法
 * TODO 3.创建 EventLoopGroup
 * TODO 4.创建ServerBootstrap
 * TODO 5.指定使用 NIO 的传输 Channel
 * TODO 6.设置 socket 地址使用所选的端口
 * TODO 7.添加 EchoServerHandler 到 Channel 的ChannelPipeline
 * TODO 8.绑定的服务器;sync 等待服务器关闭
 * TODO 9.关闭 channel 和 块，直到它被关闭
 * TODO 10.关机的 EventLoopGroup，释放所有资源。
 * </pre>
 */
public class EchoServer {

  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public void start() {
    NioEventLoopGroup group = new NioEventLoopGroup();              //3  创建并分配一个 NioEventLoopGroup 实例来处理事件的处理，如接受新的连接和读/写数据

    ServerBootstrap bootstrap = new ServerBootstrap();              //4 创建 ServerBootstrap 实例来引导服务器并随后绑定【再加上各种配置】
    bootstrap.group(group)
        .channel(NioServerSocketChannel.class)                  //5 指定信道类型
        .localAddress(
            new InetSocketAddress(port))              //6 指定本地 InetSocketAddress 给服务器绑定===》来监听新的连接请求
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {

            //------------------关键---------------------
            // 在这里我们使用一个特殊的类，ChannelInitializer 。
            // 当一个新的连接被接受，一个新的子 Channel 将被创建， ChannelInitializer 会添加我们EchoServerHandler 的实例到 Channel 的 ChannelPipeline。
            // 正如我们如前所述，这个处理器将被通知如果有入站信息。
            //------------------关键---------------------
            ch.pipeline()
                .addLast(new EchoServerHandler());//7 通过 EchoServerHandler 实例给每一个新的 Channel 初始化

          }
        });
    try {
      //ServerBootstrap 实例调用 ServerBootstrap.bind() 来绑定服务器 ：各种初始化工作和配置监听
      ChannelFuture channelFuture = bootstrap.bind().sync();      //8 调用 sync() 的原因是当前线程阻塞===》等待绑定完成

      System.out.println(
          EchoServer.class.getName() + " started and listen on " + channelFuture.channel()
              .localAddress());

      channelFuture.channel().closeFuture().sync();               //9 等待服务器 Channel 关闭
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();                                 //10
    }
  }

  public static void main(String[] args) throws Exception {
    //args参数可以在run configurations 中设置：program arguments 的一个参数 【以空格隔开】
    if (args.length != 1) {
      System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
      return;
    }
    int port = Integer.parseInt(args[0]);                   //1
    new EchoServer(port).start();                           //2
  }
}
