package com.xujian.netty.demo.webSocketChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/*
 * description
 * <pre>
 *  TODO  注册每个channel事件对应的消息处理
 *  </pre>
 * @return
 * @date 2019/9/21 15:49
 * @author xujianghrx@gmail.com
 */
public class WebsocketChatServerInitializer extends ChannelInitializer {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    //声明pipeline的处理器链
    pipeline.addLast(new HttpServerCodec());
    pipeline.addLast(new HttpObjectAggregator(64*1024));
    pipeline.addLast(new ChunkedWriteHandler());
    pipeline.addLast(new HttpRequestHandler("/ws"));
    pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
    pipeline.addLast(new TextWebSocketFrameHandler());
  }
}
