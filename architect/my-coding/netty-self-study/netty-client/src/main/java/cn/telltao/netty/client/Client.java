package cn.telltao.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author telltao@qq.com
 * netty的客户端操作类
 * @Date 2021/7/11 18:50
 */
@Component
public class Client {


    /**
     * create a scheduled  1 thread.
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * create a nio eventLoop  and allocate 2 thread.
     */
    private EventLoopGroup group = new NioEventLoopGroup(2);

    public static final String HOST = "127.0.0.1";

    public static final int PORT = 6666;

    public static final String SUPER_HOST = "127.0.0.1";

    public static final int SUPER_PORT = 6666;
    /**
     * netty channel.
     */
    private Channel channel;

    /**
     * whether to connect.
     */
    private AtomicBoolean isConnect = new AtomicBoolean(false);

    /**
     * a singleton client class.
     */
    private static class SingletonHodler {
        private static final Client INSTANCE = new Client();
    }

    /**
     * return this(Client) instance.
     */
    public static final Client getInstance() {
        return SingletonHodler.INSTANCE;
    }

    public Client() {

    }

    public synchronized void init() {
        if (!isConnect.get()) {
            try {
                this.connect(SUPER_HOST, SUPER_PORT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void connect(String host, int port) throws Exception {
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    //分配一个无延迟的tcp channel实例
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            //ch.pipeline().addLast(new ProtobufDecoder(MessageModule));
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new ClientHandler());

                        }
                    });
            ChannelFuture channelFuture = b.connect(host, port).sync();
            // 绑定通道
            this.channel = channelFuture.channel();
            System.out.println("-- Client Start..-- ");
            //当消息返回时,传递给此通道
            this.channel.closeFuture().sync();
        }finally {
            // release and reconnection
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //每隔1秒发起一次重连
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            //reconnection
                            connect(host,port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}
