package cn.telltao.netty.listener;

import cn.telltao.netty.client.Client;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author telltao@qq.com
 * @Date 2021/7/11 18:29
 */
public class ApplicationListenerReadyEvent implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.err.println("----client--应用已经启动成功----");
        Client.getInstance().init();
    }
}
