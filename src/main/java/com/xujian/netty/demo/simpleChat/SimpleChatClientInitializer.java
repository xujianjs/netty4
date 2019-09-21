package com.xujian.netty.demo.simpleChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/*
 * description
 * <pre>
 *
 * TODO 客户端ChannelInitializer
 *
 * </pre>
 * @return
 * @date 2019/9/21 15:12
 * @author xujianghrx@gmail.com
 */
public class SimpleChatClientInitializer extends ChannelInitializer {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("encoder", new StringEncoder());
    pipeline.addLast("handler", new SimpleChatClientHandler());
  }
}
