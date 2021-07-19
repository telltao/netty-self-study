package cn.telltao.netty.client;
import cn.telltao.netty.listener.ApplicationListenerReadyEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author telltao@qq.com
 * 应用启动主类
 * @Date 2021/7/11 17:46
 */
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ClientApplication.class);
        application.addListeners(new ApplicationListenerReadyEvent());
        application.run(args);
    }
}
