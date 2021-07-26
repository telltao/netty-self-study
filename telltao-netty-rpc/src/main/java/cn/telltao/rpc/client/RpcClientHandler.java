package cn.telltao.rpc.client;

import cn.telltao.rpc.codec.RpcRequest;
import cn.telltao.rpc.codec.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author telltao@qq.com
 * 实际的业务处理器
 * @Date 2021/7/22 14:05
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private Channel channel;

    private SocketAddress remotePeer;

    private Map<String,RpcFuture> pendingRpcTable = new ConcurrentHashMap<String,RpcFuture>();



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
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        String requestId = rpcResponse.getRequestId();
        //获取到请求对象
        RpcFuture rpcFuture = pendingRpcTable.get(requestId);

        if (!Objects.isNull(rpcFuture)) {
            pendingRpcTable.remove(requestId);
            rpcFuture.done(rpcResponse);
        }

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


    /**
     * <pre>
     * @author telltao@qq.com
     *  异步发送请求的方法
     * </pre>
     * @date 2021/7/25 11:23
     * @param
     * @return
     */
    public RpcFuture sendRequest(RpcRequest request) {
        //1,将请求信息封装到 RpcFuture中
        RpcFuture rpcFuture = new RpcFuture(request);
        pendingRpcTable.put(request.getRequestId(),rpcFuture);
        //2,将数据发送给远端
        channel.writeAndFlush(request);

        return rpcFuture;
    }
}
