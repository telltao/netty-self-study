package cn.telltao.rpc.test.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Liu Tao
 * @Date 2021/10/29 12:02
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws Exception {

        // 创建一个自定义大小的线程池,核心大小为16,最大16,当队列满载时,其他线程排队时间,排队队列大小
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("ThreadTest-poolExecutor").build();
        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(16, 16, 600L,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536), namedThreadFactory);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        AtomicInteger count = new AtomicInteger(0);

        poolExecutor.execute(() -> {
            try {
                count.getAndAdd(1);
            } finally {
                // 计数
                countDownLatch.countDown();
            }
        });
        poolExecutor.execute(() -> {
            try {
                count.getAndAdd(1);
            } finally {
                // 计数
                countDownLatch.countDown();
            }
        });

        try {
            // 等待
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程执行数量为:" + count.get());
    }
}
