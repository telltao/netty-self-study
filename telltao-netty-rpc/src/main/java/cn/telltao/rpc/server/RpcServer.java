package cn.telltao.rpc.server;

import cn.telltao.rpc.client.RpcClientHandler;
import cn.telltao.rpc.codec.RpcDecoder;
import cn.telltao.rpc.codec.RpcEncoder;
import cn.telltao.rpc.codec.RpcRequest;
import cn.telltao.rpc.codec.RpcResponse;
import cn.telltao.rpc.config.provider.ProviderConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * @Date 2021/7/24 13:57
 */
@Slf4j
public class RpcServer {

    private String serverAddress;

    private EventLoopGroup boosGroup = new NioEventLoopGroup();

    private EventLoopGroup workGroup = new NioEventLoopGroup();

    // key : providerConfig.insterface (接口权限命名)
    // values: providerConfig.Ref (接口的实例对象[方法])
    private volatile Map<String, Object> handlerMap = new HashMap<>();


    public RpcServer(String serverAddress) throws InterruptedException {
        this.serverAddress = serverAddress;
        this.start();

    }

    private void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                // tcp = sync +accept = backlog
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();
                        // 编码的handler 服务端编码使用返回对象
                        pipeline.addLast(new RpcEncoder(RpcResponse.class));

                        pipeline.addLast(new LengthFieldBasedFrameDecoder(66535,
                                0, 4, 0, 0));
                        // 解码的handler 服务端解码使用请求对象
                        pipeline.addLast(new RpcDecoder(RpcRequest.class));
                        //自定义handler
                        pipeline.addLast(new RpcServerHandler(handlerMap));

                    }
                });
        String[] array = serverAddress.split(":");
        String host = array[0];
        Integer port = Integer.parseInt(array[1]);

        ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info(" server success bing to {}", serverAddress);
                } else {
                    log.info("server fail bing to {}", serverAddress);
                    throw new Exception("server start fail, cause" + future.cause());
                }
            }
        });

        try {

            channelFuture.await(5000, TimeUnit.MICROSECONDS);
            if (channelFuture.isSuccess()) {
                log.info("start rpc success!");
            }
        } catch (InterruptedException ex) {
            log.error("start rpc occur Interrupted,ex:" + ex);
        }

    }

    /**
     * 程序注册器
     */
    public void registerProcessor(ProviderConfig config) {

        handlerMap.put(config.getInterfaceClass(), config.getRef());
    }

    /**
     * 关闭资源 group worker
     */
    public void close() {
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


}
