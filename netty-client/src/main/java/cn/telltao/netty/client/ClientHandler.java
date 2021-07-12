package cn.telltao.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * 创建一个客户端的操作者
 * @Date 2021/7/11 19:34
 */
public class ClientHandler  extends ChannelInboundHandlerAdapter {
    /**
     * 创建一个线程组 在此不再过于描述 请 google
     */
    ThreadPoolExecutor workerPool = new ThreadPoolExecutor(5,
            10,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4000),
            new ThreadPoolExecutor.DiscardPolicy());


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        super.channelRead(ctx, msg);
    }
}
