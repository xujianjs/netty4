package com.xujian.netty.demo.simpleChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * description
 * <pre>
 *  TODO 服务端处理器--主要完成业务逻辑处理
 * </pre>
 *
 * @author xujianghrx@gmail.com
 * @date 2019/9/21 14:30
 */
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {// (1)

  /**
   * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group. A
   * closed Channel is automatically removed from the collection,
   */
  protected static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


  /**
   * description
   * <pre>
   *  TODO  Is called for each message of type {@link I}. I指代处理器接收到的泛型,这里就是String
   * </pre>
   *
   * @return void
   * @date 2019/9/21 14:54
   * @author xujianghrx@gmail.com
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {// (4)
    Channel incoming = ctx.channel();
    for (Channel channel : channels) {
      if (channel != incoming) {
        channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
      } else {
        channel.writeAndFlush("[you]" + msg + "\n");
      }
    }
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
//    super.handlerAdded(ctx);

    Channel incoming = ctx.channel();

    //向其他信道广播消息
    ctx.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
    //把当前信道注册到全局信道集合
    channels.add(incoming);

  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {// (3)
//    super.handlerRemoved(ctx);
    Channel incoming = ctx.channel();

    // Broadcast a message to multiple Channels
    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");

    // A closed Channel is automatically removed from ChannelGroup,
    // so there is no need to do "channels.remove(ctx.channel());"

  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {// (5)
//    super.channelActive(ctx);

    //获取当前访问者的channel（channel）
    //A nexus to a network socket or a component which is capable of I/O
    // * operations such as read, write, connect, and bind.

    Channel incoming = ctx.channel();
    System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "在线");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {// (6)
//    super.channelInactive(ctx);
    Channel incoming = ctx.channel();
    System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    super.exceptionCaught(ctx, cause);
    Channel incoming = ctx.channel();
    System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "异常");
    // 当出现异常就关闭连接
    cause.printStackTrace();
    ctx.close();
  }
}
