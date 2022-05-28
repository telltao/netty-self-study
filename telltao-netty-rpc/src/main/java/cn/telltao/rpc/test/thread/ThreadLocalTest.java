package cn.telltao.rpc.test.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Liu Tao
 * @Date 2021/10/29 11:49
 */
public class ThreadLocalTest {

    public static void main(String[] args) throws Exception {

        // 创建一个自定义大小的线程池,核心大小为16,最大16,当队列满载时,其他线程排队时间,排队队列大小
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("ThreadTest-poolExecutor").build();
        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(16, 16, 600L,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536),namedThreadFactory);

        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        Thread.sleep(500);
        poolExecutor.execute(() -> {
            try {
                threadLocal.set("qew");
                // Do some thing ...

                String s = threadLocal.get();

                System.out.println(s);
                // Do some thing ...

            } finally {
                threadLocal.remove();
            }
        });
        poolExecutor.shutdown();
    }
}
