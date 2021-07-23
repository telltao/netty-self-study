package cn.telltao.rpc.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;

/**
 * @author telltao@qq.com
 * 实际的业务处理器
 * @Date 2021/7/22 14:05
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

    private Channel channel;

    private SocketAddress remotePeer;


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }


    public SocketAddress getRemotePeer() {
        return  this.remotePeer;
    }

    /**
     * 关闭连接
     */
    public void close() {
        // netty提供了关闭连接的方法 即发送一个空的buffer 这样该事件就会监听到连接并关闭
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
