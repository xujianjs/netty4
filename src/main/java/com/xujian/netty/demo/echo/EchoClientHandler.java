package com.xujian.netty.demo.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.nio.Buffer;

/**
 * description
 *
 * <pre>
 * TODO 1. @Sharable 标记这个类的实例可以在 channel 里共享
 * TODO 2.当被通知该 channel 是活动的时候就发送信息
 * TODO 3.记录接收到的消息
 * TODO 4.记录日志错误并关闭 channel
 * </pre>
 *
 *
 *
 * @author xujianghrx@gmail.com
 * @date 2019/9/19 22:20
 */
@Sharable   //1
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    super.channelActive(ctx);
    ctx.writeAndFlush(Unpooled.copiedBuffer("netty rocks!", CharsetUtil.UTF_8)); //2
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//    super.channelRead(ctx, msg);
    ByteBuf in = (ByteBuf) msg;
    System.out.printf("客户端 接收消息：%s", in.toString(CharsetUtil.UTF_8));//3
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();//4
    ctx.close();
  }
}
