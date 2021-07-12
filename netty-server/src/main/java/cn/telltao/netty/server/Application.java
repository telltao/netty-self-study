package cn.telltao.netty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import cn.telltao.netty.listener.ApplicationListenerReadyEvent;


/**
 * @author telltao@qq.com
 * 应用启动主类
 * @Date 2021/7/11 17:46
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationListenerReadyEvent());
        application.run(args);
    }
}
