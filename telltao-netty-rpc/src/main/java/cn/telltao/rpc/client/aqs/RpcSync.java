package cn.telltao.rpc.client.aqs;

import cn.telltao.rpc.client.RpcCallback;
import cn.telltao.rpc.client.RpcFuture;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author telltao@qq.com
 * 一个对象锁
 * @Date 2021/7/25 11:59
 */
public class RpcSync extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 6661638304537933553L;

    /**
     * 定义两把锁的标记
     * 完成为 1 等待中为 0
     */
    private final int done = 1;
    private final int pending = 0;

    @Override
    protected boolean tryAcquire(int arg) {
        return super.getState() == done ? true : false;
    }

    @Override
    protected boolean tryRelease(int arg) {
        if (super.getState() == pending) {
            // 判断是否是预期值-> pending   (需要 pending状态,实际是done状态)
            if (super.compareAndSetState(pending, done)) {
                return true;
            }
            return false;
        }
        return super.tryRelease(arg);
    }

    public boolean isDone() {
        return super.getState() == done;
    }


}
