package cn.telltao.guidance.chapter2.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author telltao
 * 创建一个处理入站时间的操作者
 * @Date 2021/6/9 17:09
 */
@Sharable //标记当前类可被多个 Channel共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //记录已接收消息的转储
        System.out.println("客户端收到消息: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知 Channel是活跃的时候，发送一条消息
        //告诉毛毛，我是吉吉国王
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hi~ Mao Mao ,I'm a monkey!",CharsetUtil.UTF_8));
    }

    /**
     * 在发生异常时，记录错误并关闭Channel
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印堆栈日志并关闭当前操作者
        cause.printStackTrace();
        ctx.close();
        System.err.println("客户端遇到异常，已关闭当前通道");

    }
}
