package com.xujian.netty.demo.webSocketChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/*
 * description
 *
 * <pre>
 * TODO 根据单一职责原则  此处理器只完成http的请求处理
 *
 *
 * </pre>
 * @return
 * @date 2019/9/21 15:20
 * @author xujianghrx@gmail.com
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

  private final String wsUri;
  private static final File INDEX;

  static {
    //获取源码资源路径对应的URL  主要用于映射和定位html文件
    URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
    try {
      String path = location.toURI() + "WebsocketChatClient.html";
      path = !path.contains("file:") ? path : path.substring(5);
      INDEX = new File(path);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
    }
  }


  public HttpRequestHandler(String wsUri) {
    this.wsUri = wsUri;
  }

  public String getWsUri() {
    return wsUri;
  }

  public static File getINDEX() {
    return INDEX;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    if (wsUri.equalsIgnoreCase(request.uri())) {
      ctx.fireChannelRead(request.retain());
    } else {
      if (HttpUtil.is100ContinueExpected(request)) {
        send100Continue(ctx);
      }

      RandomAccessFile file = new RandomAccessFile(INDEX, "r");

      DefaultHttpResponse httpResponse = new DefaultHttpResponse(
          request.protocolVersion(), HttpResponseStatus.OK);
      httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");//HTML

      boolean keepAlive = HttpUtil.isKeepAlive(request);

      if (keepAlive) {
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
        httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
      }

      //通过ctx写响应消息并刷新
//      ctx.writeAndFlush(httpResponse);

      ctx.write(httpResponse);                    //6

      if (ctx.pipeline().get(SslHandler.class) == null) {     //7
        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
      } else {
        ctx.write(new ChunkedNioFile(file.getChannel()));
      }

      ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);           //8
      if (!keepAlive) {
        future.addListener(ChannelFutureListener.CLOSE);        //9
      }

      //关闭文件通道
      file.close();

    }
  }

  private static void send100Continue(ChannelHandlerContext ctx) {
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
    ctx.writeAndFlush(response);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
      throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("Client:"+incoming.remoteAddress()+"异常");
    // 当出现异常就关闭连接
    cause.printStackTrace();
    ctx.close();
  }
}
