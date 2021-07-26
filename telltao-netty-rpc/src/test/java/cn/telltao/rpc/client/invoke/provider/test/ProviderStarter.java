package cn.telltao.rpc.client.invoke.provider.test;

import cn.telltao.rpc.config.provider.ProviderConfig;
import cn.telltao.rpc.config.provider.RpcServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author telltao@qq.com
 * 硬编码构建netty服务端
 * @Date 2021/7/26 13:40
 */
public class ProviderStarter {


    public static void main(String[] args) {

         final int port = 9000;

        //	服务端启动
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // 每一个具体的服务提供者的配置类
                    ProviderConfig providerConfig = new ProviderConfig();
                    providerConfig.setInterfaceClass("cn.telltao.rpc.client.invoke.consumer.test.HelloService");
                    HelloServiceImpl hellpHelloServiceImpl = HelloServiceImpl.class.newInstance();
                    providerConfig.setRef(hellpHelloServiceImpl);

                    //	把所有的ProviderConfig 添加到集合中
                    List<ProviderConfig> providerConfigs = new ArrayList<ProviderConfig>();
                    providerConfigs.add(providerConfig);

                    RpcServerConfig rpcServerConfig = new RpcServerConfig(providerConfigs);
                    rpcServerConfig.setPort(port);
                    rpcServerConfig.exporter();

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
