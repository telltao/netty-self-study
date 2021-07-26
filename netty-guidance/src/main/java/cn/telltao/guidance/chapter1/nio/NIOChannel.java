package cn.telltao.guidance.chapter1.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author telltao
 * 创建一个异步网络编程 一次请求只可处理一次连接
 * @Date 2021/6/7 20:56
 */
public class NIOChannel {

    public static void main(String[] args) {
        // 终端执行  telnet localhost 7778
        //输出：Hi~ Mao Mao ,I'm a monkey!
        nioServer(7778);
    }

    /**
     * <pre>
     * 好了，我们已经创建好了一个异步阻塞的服务，并可正常接收和发送数据<br/>
     * 首先，我们要知道，JDK1.4新增了对NIO支持的套接字 使用selector来对连接消息进行轮询处理消息<br/>
     * 在这个科技发展的时代，硬件相比往年显的不是很宝贵，
     * 当然这个简单的demo 也可能 会保证让我们处理10000条并发连接，并不会丢失数据<br/>
     * 使用较少的线程便可以处理许多连接， 因此也减少了内存管理和上下文切换所带来开销；
     * 代码量很长，就像我们每次使用jdbc,使用inputstream,outputstream，都需要各种开启和关闭。。以及可能会有其他潜在问题。。
     * 如果业务涉及范围很广，可想可知，需要堆各种代码。。
     * </pre>
     * @see  NettyNIOChannel
     */
    public static  void  nioServer(int port){

        try {
            //使用JDK1.4新增的异步阻塞模型Channel
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            socketChannel.configureBlocking(false);
            ServerSocket socket = socketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            //为该套接字绑定端口
            socket.bind(address);
            //打开该selector 来进行轮训处理数据
            Selector selector = Selector.open();
            //我们在上面声明了socketChannel 但是需要在其上注册Selector，才可以建立连接
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //你好，毛毛，我是吉吉国王
            final ByteBuffer msg = ByteBuffer.wrap("Hi~ Mao Mao ,I'm a monkey!\r\n".getBytes());
            for (;;){
                try {
                    //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
                    selector.select();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                //获取所有接收事件的SelectionKey实例
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    try {
                        //检查事件是否是一个新的已经就绪可以被接受的连接
                        if (key.isAcceptable()) {
                            ServerSocketChannel server =
                                    (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            //接受客户端，并将它注册到选择器
                            client.register(selector, SelectionKey.OP_WRITE |
                                    SelectionKey.OP_READ, msg.duplicate());
                            System.out.println(
                                    "Accepted connection from " + client);
                        }
                        //检查套接字是否已经准备好写数据
                        if (key.isWritable()) {
                            SocketChannel client =
                                    (SocketChannel) key.channel();
                            ByteBuffer buffer =
                                    (ByteBuffer) key.attachment();
                            while (buffer.hasRemaining()) {
                                //将数据写到已连接的客户端
                                if (client.write(buffer) == 0) {
                                    break;
                                }
                            }
                            //关闭连接
                            client.close();
                        }
                    } catch (IOException ex) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException cex) {
                            //关闭连接一般不会异常
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
