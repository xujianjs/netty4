package com.xujian.netty.demo.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * @author 徐健
 * @version 1.0.0
 * @ClassName EchoServerHandler.java
 * @Description
 * TODO 服务器handler
 * TODO 将接受到的数据的拷贝发送给客户端  因此我们需要实现 {@link ChannelInboundHandler}接口来定义处理入站事件的方法
 * TODO 由于应用简单，只需要继承ChannelInboundHandlerAdapter  该类提供了默认ChannelInboundHandler的实现
 * TODO 所以只需要覆盖下面的方法：
 * TODO channelRead() - 每个信息入站都会调用
 * TODO channelReadComplete() - 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
 * TODO exceptionCaught()- 读操作时捕获到异常时调用
 * @createTime 2019年09月19日 16:52:00
 *
 *
 *
 *
 *
 * @see ChannelInboundHandler
 *
 *
 *
 *
 * 1.@Sharable  标识这类的实例之间可以在 channel 里面共享
 * 2.日志消息输出到控制台
 * 3.将所接收的消息返回给发送者。注意，这还没有冲刷数据
 * 4.冲刷所有待审消息到远程节点。关闭通道后，操作完成
 * 5.打印异常堆栈跟踪
 * 6.关闭通道
 *
 */
@ChannelHandler.Sharable                                                                                             //1
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf in = (ByteBuf) msg;
        System.out.printf("服务端 接收消息：%s",in.toString(CharsetUtil.UTF_8));
        ctx.write(in);//把入站消息写入到处理器上下文                                                                  //3
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)                                                                     //4
        .addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();                                                                                    //5
        ctx.close();                                                                                                //6
    }
}
