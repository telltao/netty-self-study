package cn.telltao.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author telltao@qq.com
 * 负责管理通道中的连接信息,负责 释放,新建,关闭,重连等
 * @Date 2021/7/20 17:00
 */
@Slf4j
public class RpcConnectManager {

    private static volatile RpcConnectManager rpcConnectManager = new RpcConnectManager();

    private RpcConnectManager() {

    }

    public static RpcConnectManager getInstance() {
        return rpcConnectManager;
    }

    /**
     * 一个连接的地址,对应一个实际的业务处理器
     */
    private Map<InetSocketAddress, RpcClientHandler> connectedHandlerMap = new ConcurrentHashMap<>();

    /**
     * 所有连接成功的地址,所对应的任务执行器列表
     */
    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlerList = new CopyOnWriteArrayList<>();
    /**
     * 异步提交连接请求的线程池
     */
    private ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(16, 16, 60,
                    TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(65536));

    /**
     * 事件轮询组
     */
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    // 重入互斥锁
    private ReentrantLock connectedLock = new ReentrantLock();
    //connectedLock 锁的实例
    private Condition connectedCondition = connectedLock.newCondition();

    //连接超时时间 6秒
    private long connectedTimeoutMills = 6000;
    // 是否运行状态
    private volatile boolean isRunning = true;

    // AtomicInteger 本身已经修饰 volatile, 此处加的目的是给自己看的...
    private volatile AtomicInteger handlerIdx = new AtomicInteger(0);

    //1,异步连接 连接失败监听,连接成功监听

    //2, 将连接的资源进行缓存

    public void connect(final String serverAddress) {
        List<String> allServerAddress = Arrays.asList(serverAddress.split(","));
        updateConnectedServer(allServerAddress);

    }

    /**
     * <pre>
     * @author telltao@qq.com
     *   更新缓存信息,并异步发起连接
     * </pre>
     *
     * @param
     * @return
     * @date 2021/7/22 13:44
     */
    public void updateConnectedServer(List<String> allServerAddress) {

        if (CollectionUtils.isNotEmpty(allServerAddress)) {
            //将两个地址临时存储到结集合中
            HashSet<InetSocketAddress> allServerNodeSet = new HashSet<>();

            allServerAddress.forEach(item -> {
                String[] array = item.split(":");
                if (array.length == 2) {
                    String host = array[0];
                    int port = Integer.valueOf(array[1]);
                    final InetSocketAddress remotePeer = new InetSocketAddress(host, port);
                    allServerNodeSet.add(remotePeer);
                }
            });

            //2,调用建立连接方法,发起连接
            allServerNodeSet.forEach(inetSocketAddress -> {
                //只存储不存在的连接
                if (!connectedHandlerMap.keySet().contains(inetSocketAddress)) {
                    connectAsync(inetSocketAddress);
                }
            });

            // 3,如果 allServerAddress里不存在的地址,需要将它移除,并释放handler中的资源

            connectedHandlerList.forEach(item -> {
                RpcClientHandler rpcClientHandler = item;
                SocketAddress remotePeer = rpcClientHandler.getRemotePeer();
                if (!allServerNodeSet.contains(remotePeer)) {
                    log.info("remove invalid server node :{}", remotePeer);
                    //释放资源
                    RpcClientHandler removeHandler = connectedHandlerMap.get(remotePeer);
                    if (!Objects.isNull(removeHandler)) {
                        removeHandler.close();
                    }
                    connectedHandlerList.remove(remotePeer);
                }
            });

        } else {
            //添加告警,并清除所有的缓存信息
            log.error("no available server address! ");
            clearConnected();
        }
    }


    private void connectAsync(InetSocketAddress remotePeer) {
        threadPoolExecutor.submit(() -> {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new RpcClientInitializer());
            connect(b, remotePeer);
        });
    }

    private void connect(final Bootstrap b, InetSocketAddress remotePeer) {

        // 1.建立连接
        final ChannelFuture channelFuture = b.connect(remotePeer);

        // 2,连接失败添加监听,清除资源并重连
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("channelFuture.channel close operationComplete, remote = {}", remotePeer);
                future.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //清空之前的连接
                        log.warn("connect fail,to reconnect! ");
                        clearConnected();
                    }
                }, 3, TimeUnit.SECONDS);
            }
        });

        //3,连接成功添加监听,将新连接放入缓存
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info(" successfully connect to remote server, remote peer = {}", remotePeer);
                    RpcClientHandler handler = future.channel().pipeline().get(RpcClientHandler.class);
                    addHandler(handler);
                }
            }
        });
    }


    /**
     * 连接失败时,释放资源并清空缓存
     * 先删除所有的map(connectedHandlerMap)中的数据,
     * 再删除list(connectedHandlerList)中的数据
     */
    private void clearConnected() {

        if (CollectionUtils.isEmpty(connectedHandlerList)) {
            return;
        }
        connectedHandlerList.forEach(rpcClientHandler -> {
            // 通过rpcHandler 找到具体的地址(remotePeer) 从  connectedHandlerMap中移除指定的 InetSocketAddress
            SocketAddress remotePeer = rpcClientHandler.getRemotePeer();
            RpcClientHandler handler = connectedHandlerMap.get(remotePeer);
            if (!Objects.isNull(handler)) {
                handler.close();
                connectedHandlerMap.remove(remotePeer);
            }
        });

        connectedHandlerList.clear();

    }

    /**
     * 添加RpcClientHandler到指定的缓存中
     * connectedHandlerMap, connectedHandlerList
     *
     * @param handler
     */
    private void addHandler(RpcClientHandler handler) {
        connectedHandlerList.add(handler);
        InetSocketAddress remoteAddress = (InetSocketAddress) handler.getRemotePeer();
        connectedHandlerMap.put(remoteAddress, handler);
        // singnalAvailableHandler 唤醒可用的业务执行器
        singnalAvailableHandler();
    }

    /**
     * 唤醒另外一端的线程(阻塞的状态中),告知有新连接接入
     */
    private void singnalAvailableHandler() {

        connectedLock.lock();
        try {
            //唤醒所有等待的线程
            connectedCondition.signalAll();
        } finally {
            connectedLock.unlock();
        }
    }

    /**
     * 等待新连接接入的方法
     *
     * @return
     * @throws InterruptedException
     */
    private boolean waitingForAvailableHandler() throws InterruptedException {
        connectedLock.lock();
        try {
            //唤醒所有等待的线程
            connectedCondition.await(this.connectedTimeoutMills, TimeUnit.SECONDS);
        } finally {
            connectedLock.unlock();
        }
    }

    /**
     * 选择一个实际的业务处理器
     *
     * @return
     */
    public RpcClientHandler chooseHandler() {

        CopyOnWriteArrayList<RpcClientHandler> handlers =
                (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlerList.clone();
        int size = handlers.size();
        while (isRunning && size <= 0) {
            try {
                boolean available = waitingForAvailableHandler();
                //唤醒所有等待的线程后,需要重新获取连接的数据
                if (available) {
                    handlers = (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlerList.clone();
                    size = handlers.size();
                }
            } catch (InterruptedException ex) {
                log.error("wating for available node is interrupted!");
                throw new RuntimeException("no connect any servers!", ex);
            }
        }

        if (!isRunning) {
            return null;
        }

        //取模获取其中的一个业务处理器
        int index = (handlerIdx.getAndAdd(1) + size) % size;
        return handlers.get(index);

    }

    /**
     * 关闭服务方法
     */
    public void stop() {
        isRunning = false;
        if (CollectionUtils.isEmpty(connectedHandlerList)) {
            return;
        }
        connectedHandlerList.forEach(item -> {
            RpcClientHandler rpcClientHandler = item;
            rpcClientHandler.close();
        });

        // 在这里要调用一下唤醒操作
        singnalAvailableHandler();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();

    }

    /**
     * 发起重连的方法
     * 1,释放资源
     * 2,异步重新连接
     *
     * @param handler
     * @param remotePeer
     */
    public void reconnect(final RpcClientHandler handler, final SocketAddress remotePeer) {
        //释放资源
        if (!Objects.isNull(handler)) {
            handler.close();
            connectedHandlerList.remove(handler);
            connectedHandlerMap.remove(remotePeer);
        }
        //重新连接
        connectAsync((InetSocketAddress) remotePeer);
    }


}
