package cn.telltao.guidance.chapter2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author telltao
 * 创建了一个简单的服务端引导,
 * 当然，你也可以使用jmeter 对当前服务端进行并发进行压力测试,并查看效果
 * @Date 2021/6/12 15:17
 */
public class EchoServer {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        new EchoServer().start(port);
    }

    public void start(int port) throws Exception {
        //自定义服务端的操作者
        EchoServerHandler echoServerHandler = new EchoServerHandler();
        // 1，创建一个事件轮询组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //2，创建一个服务端的引导
            ServerBootstrap bootstrap = new ServerBootstrap();
            //3，绑定组
            bootstrap.group(group)
                    //4， 指定为异步的服务端套接字通道
                    .channel(NioServerSocketChannel.class)
                    //5，绑定到本机端口，用来监听客户端发送的数据包
                    .localAddress(new InetSocketAddress(port))
                    //添加子操作者
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // echoServerHandler标注为了@shareable  也就是说
                            //可以将这个handler添加到不同的 pipeline中 ，而不会出现锁竞争
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });
            //6，异步绑定服务器，sync()进行阻塞，直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            //7 获取 Channel的 closeFuture,并阻塞当前线程，直到它完成
            future.channel().closeFuture().sync();

        } finally {
            // 8 关闭事件轮询组，释放资源
            group.shutdownGracefully().sync();
        }

    }
}
