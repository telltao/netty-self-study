package cn.telltao.test.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Liu Tao
 * @Date 2021/10/15 15:06
 */
public class ThreadTest {
    public static void main(String[] args) {

    }

    /**
     * jdk 1.5创建线程
     */
    void jdk5(){

        // 创建缓存线程池
        ExecutorService executorCachedService = Executors.newCachedThreadPool();
        executorCachedService.submit(() -> exec());

        // 创建固定长度的线程池
        ExecutorService executorFixedService1 = Executors.newFixedThreadPool(10);
        executorFixedService1.submit(() -> exec());
        // 创建延迟启动的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.submit(() -> exec());


        // 创建一个自定义大小的线程池,核心大小为16,最大16,当队列满载时,其他线程排队时间,排队队列大小
        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(16, 16, 600L,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));


    }

    private void exec() {
    }

}





/**
 * @author Liu Tao
 *  Jdk 1.5 before
 * @date 2021/10/15 15:11
 */
class MyThread extends Thread {
    @Override
    public void run() {
            //
    }
}

class MyThreadOther implements Runnable {
    @Override
    public void run() {

    }
}

class MyThreadTwo implements Callable {
    @Override
    public Object call() throws Exception {
        return null;
    }
}
