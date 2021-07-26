package cn.telltao.rpc.client.invoke.consumer.test;

/**
 * @author telltao@qq.com
 * @Date 2021/7/26 13:31
 */
public interface HelloService {

    String hello (String name);

    String hello(User user);
}
