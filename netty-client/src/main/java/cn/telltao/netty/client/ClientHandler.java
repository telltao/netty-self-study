package cn.telltao.netty.client;

import cn.telltao.common.protobuf.MessageModule;
import cn.telltao.execute.MessageTaskResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * 创建一个客户端的操作者 请注意,异步操作继承 ChannelInboundHandlerAdapter 否继承 SimpleChannelInboundHandler
 * @Date 2021/7/11 19:34
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 创建一个线程组
     */
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            10,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4000),
            new ThreadPoolExecutor.DiscardPolicy());


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageModule.Message response = (MessageModule.Message) msg;
        //异步处理
        executor.submit(new MessageTaskResponse(response, ctx));

    }
}
