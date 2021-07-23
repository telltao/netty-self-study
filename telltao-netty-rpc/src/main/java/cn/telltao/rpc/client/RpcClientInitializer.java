package cn.telltao.rpc.client;

import cn.telltao.rpc.codec.RpcDecoder;
import cn.telltao.rpc.codec.RpcEncoder;
import cn.telltao.rpc.codec.RpcRequest;
import cn.telltao.rpc.codec.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author telltao@qq.com
 * @Date 2021/7/22 14:17
 */
public class RpcClientInitializer  extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        // 编码的handler
        pipeline.addLast(new RpcEncoder(RpcRequest.class));
        /**
         * netty提供的一个解码器 帮我们去解析数据包
         *
         maxFrameLength – 帧的最大长度。 如果帧的长度大于此值，则会抛出TooLongFrameException 。
         lengthFieldOffset – 长度字段的偏移量
         lengthFieldLength – 长度字段的长度
         lengthAdjustment – 添加到长度字段值的补偿值
         initialBytesToStrip – 从解码帧中剥离的第一个字节数
         */
        pipeline.addLast(new LengthFieldBasedFrameDecoder(66535,
                0,4,0,0));
        // 解码的handler
        pipeline.addLast(new RpcDecoder(RpcResponse.class));
        //自定义handler
        pipeline.addLast(new RpcClientHandler());


        //实际的业务处理器RpcClientHandler

    }
}
