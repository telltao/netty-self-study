package cn.telltao.rpc.client;

import cn.telltao.rpc.client.aqs.RpcSync;
import cn.telltao.rpc.codec.RpcRequest;
import cn.telltao.rpc.codec.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author telltao@qq.com
 * RPC异步回调返回类
 * @Date 2021/7/25 11:23
 */
@Slf4j
public class RpcFuture implements Future<Object> {

    private RpcRequest rpcRequest;

    private RpcResponse rpcResponse;

    private long startTime;

    private final long TIMET_HRESHOLD = 5000;

    /**
     * 等待回调的list
     */
    private List<RpcCallback> pendingCallbacks = new ArrayList<>();

    private RpcSync rpcSync;

    private ReentrantLock lock = new ReentrantLock();
    /**
     * 异步线程 用作异步提交处理
     */
    private ThreadPoolExecutor executor =
            new ThreadPoolExecutor(16, 16, 60,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    public RpcFuture(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        this.startTime = System.currentTimeMillis();
        this.rpcSync = new RpcSync();
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  实际的回调处理
     * </pre>
     *
     * @date 2021/7/25 11:37
     */
    public void done(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        //以独占模式发布
        boolean release = rpcSync.release(1);
        if (release) {
            invokeCallbacks();
        }

        long costTime = System.currentTimeMillis() - startTime;
        if (TIMET_HRESHOLD < costTime) {
            log.warn("the rpc response time is too slow, request id ={},method={},costTime ={}"
                    , rpcRequest.getRequestId(), rpcRequest.getMethodName(), costTime);
        }


    }

    /**
     * 依次执行回调函数
     */
    private void invokeCallbacks() {
        if (CollectionUtils.isEmpty(pendingCallbacks)) {
            return;
        }
        lock.lock();
        try {
            pendingCallbacks.forEach(callback -> {
                runCallback(callback);
            });
        } finally {
            lock.unlock();
        }

    }

    private void runCallback(RpcCallback callback) {
        final RpcResponse rpcResponse = this.rpcResponse;

        executor.submit(() -> {
            if (Objects.isNull(rpcResponse.getThrowable())) {
                callback.sueecess(rpcResponse.getResult());
            } else {
                callback.failure(rpcResponse.getThrowable());
            }

        });

    }

    /**
     * 在应用执行的过程中,添加任务至执行队列中
     * @return
     */
    public RpcFuture addCallback(RpcCallback callback) {
        lock.lock();
        try {
            //判断异步是否执行成功
            if (rpcSync.isDone()) {
                //执行成功后,执行回调方法
                runCallback(callback);
            } else {
                //还未成功,添加到执行队列中
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {

        return rpcSync.isDone();
    }

    /**
     * 发送请求后 获取结果
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        // 以独占模式获取，忽略中断 这个 arg值是瞎写的 方法中未被引用,你可以随便写(不提倡)
        // 实际上调用到了 rpcSync.tryAcquire();
        rpcSync.acquire(-1);
        if (!Objects.isNull(this.rpcResponse)) {
            return this.rpcResponse.getResult();
        }

        return null;
    }

    /**
     * 获取许可,并判断是否获取成功
     */
    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        boolean success = rpcSync.tryAcquireNanos(-1, unit.toNanos(timeout));
        //独占模式获取成功
        if (success) {
            if (!Objects.isNull(this.rpcResponse)) {
                return this.rpcResponse.getResult();
            } else {
                return null;
            }
        }
        throw new RuntimeException("timeout exception requestId:" + this.rpcRequest.getRequestId()
                + "className:" + this.rpcRequest.getClassName()
                + "methodName:" + this.rpcRequest.getMethodName());
    }


}
