package com.xujian.netty.demo.simpleChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleChatServerInitializer extends ChannelInitializer {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    //获取channel绑定的pipeline
    ChannelPipeline pipeline = ch.pipeline();

    //注册处理器链
    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("encoder", new StringEncoder());
    pipeline.addLast("handler", new SimpleChatServerHandler());

    System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");

  }
}
