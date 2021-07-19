package cn.telltao.netty.server;

import cn.telltao.common.protobuf.MessageModule;
import cn.telltao.execute.MessageTaskRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * 创建一个服务端的操作者
 * @Date 2021/7/12 17:41
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            10,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4000),
            new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MessageModule.Message request = (MessageModule.Message) msg;
        executor.submit(new MessageTaskRequest(request, ctx));
    }
}
