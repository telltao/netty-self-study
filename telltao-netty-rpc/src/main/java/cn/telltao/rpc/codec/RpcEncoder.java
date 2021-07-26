package cn.telltao.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author telltao@qq.com
 * 编码器
 * @Date 2021/7/23 20:17
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 1,将对应的java对象进行编码
     * 2,将内容填充到buffer中
     * 3,将消息冲刷出站至server端
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        if (genericClass.isInstance(msg)) {
            byte[] data = SerializationUtils.serialize(msg);
            // 消息分为 1,包头 数据包长度  2,包体
            out.writeBytes(data);
        }
    }
}
