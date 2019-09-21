package com.xujian.netty.demo.simpleChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println(msg);
  }
}
