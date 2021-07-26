package cn.telltao.rpc.client.invoke.provider.test;

import cn.telltao.rpc.client.invoke.consumer.test.HelloService;
import cn.telltao.rpc.client.invoke.consumer.test.User;

/**
 * @author telltao@qq.com
 * @Date 2021/7/26 13:35
 */
public class HelloServiceImpl  implements HelloService {

    @Override
    public String hello(String name) {
        //告诉毛毛,我是吉吉国王
        System.err.println("---------服务调用-------------");
        return "Hi~ "+name+" ,I'm a monkey!";
    }

    @Override
    public String hello(User user) {
        // 你好吉吉国王,我是 毛毛
        return "Hi~ monkey ,I'm a !"+user.getName();
    }
}
