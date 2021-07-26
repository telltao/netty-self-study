package cn.telltao.guidance.chapter1.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author telltao
 * 使用netty来实现异步网络编程
 * @Date 2021/6/7 20:57
 */
public class NettyNIOChannel {
    public static void main(String[] args) throws Exception {
        nioServer(7778);
    }

    /**
     * <pre>
     *      在此处我们使用netty帮助我们编写了一套基于NIO的阻塞案例,当然,netty的强大之处在于异步<br/>
     *      我们只需要将事件轮询组改为 NioEventLoopGroup 将 channel改为 NioServerSocketChannel <br/>
     *
     *      我们来回顾一下以下案例要做的功能点:
     *      1,创建一个事件轮询组
     *      2,创建了本地服务引导,并绑定到本地连接,并使用阻塞IO用来接收数据
     *      3,随后,该消息是这样在pipeline中传播的
     *         3.1:该消息被传递到管道的末端,交由自定义的入站Handler去解析
     *         3.2:当channelActive监测到活跃的连接后,入站事件会将自定义消息冲刷出站
     *
     *  </pre>
     */
    public static void nioServer(int port) throws Exception {

        //使用netty封装的的ByteBuf来存储数据  告诉毛毛，我是吉吉国王
        final ByteBuf buf =
                Unpooled.unreleasableBuffer(
                        Unpooled.copiedBuffer("Hi~ Mao Mao ,I'm a monkey!\r\n", Charset.forName("UTF-8")));
        //创建一个事件轮询组
        // EventLoopGroup group = new OioEventLoopGroup();
        //TODO 此处直接修改类即可改为NIO
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建一个基于netty的服务端引导类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //将这个引导类绑定到事件轮询组上
            bootstrap.group(group)
                    //绑定一个同步阻塞的通道 旧的I/O
                    // .channel(OioServerSocketChannel.class)
                    //TODO 此处直接修改类即可改为NIO
                    .channel(NioServerSocketChannel.class)
                    //将这个服务端的引导绑定到本地端口
                    .localAddress(new InetSocketAddress(port))
                    //分布一个子操作者,用来处理消息
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到当前消息的管道,并将消息冲刷到末端Handler
                            ch.pipeline().addLast(
                                    //添加了个 简单的入站Handler适配类,它是 ChannelHandlerAdapter的子类,只关注入站事件
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            //将消息写入到客户端,并在消息写完之后,关闭连接,释放资源
                                            //可能你想知道在这里是什么时候write,什么时候flush
                                            // 将ctx中的待传播的handler都write完,然后再flush 没有事件传播(写入操作)才会flush
                                            ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                        }
                                    }
                            );
                        }
                    });
            //绑定服务器以接受连接
            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
