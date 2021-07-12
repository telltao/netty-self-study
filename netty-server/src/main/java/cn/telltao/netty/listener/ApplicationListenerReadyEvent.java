package cn.telltao.netty.listener;

import cn.telltao.netty.server.Server;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;


public class ApplicationListenerReadyEvent implements ApplicationListener<ApplicationReadyEvent>{

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.err.println("----server--应用已经启动成功----");
		new Server();
	}

}
