package cn.telltao.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author telltao@qq.com
 * Rpc 解码器
 * @Date 2021/7/23 20:17
 */
public class RpcDecoder extends ByteToMessageDecoder {


    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        //可读字节小于4 则为空数据 直接返回
        if (in.readableBytes() < 4) {
            return;
        }
        //当前读取位置
        ByteBuf byteBuf = in.markReaderIndex();
        // 当前请求数据包的大小
        int dataLength = in.readInt();
        // TCP传输数据并不是全部一次传输到位的,如果当前可读字节小于数据包总长度,则返回继续等待
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        //需要读取的数据包长度
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        //解码 返回指定的对象
        Object object = SerializationUtils.deserialize(data, genericClass);
        //填充到buffer中,以便该数据包继续在pipline中传播 直至自定义handler
        out.add(object);

    }
}
