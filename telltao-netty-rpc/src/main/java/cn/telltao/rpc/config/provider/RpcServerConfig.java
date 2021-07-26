package cn.telltao.rpc.config.provider;

import cn.telltao.rpc.server.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author telltao@qq.com
 * Rpc服务端启动配置类
 * @Date 2021/7/26 13:16
 */
@Slf4j
public class RpcServerConfig {

    private final String host = "127.0.0.1";

    protected int port;

    private List<ProviderConfig> providerConfigs;

    private RpcServer rpcServer = null;

    public RpcServerConfig(List<ProviderConfig> providerConfigs) {
        this.providerConfigs = providerConfigs;
    }

    public void exporter() {
        if (Objects.isNull(rpcServer)) {

            try {
                rpcServer = new RpcServer((host + ":" + port));
            } catch (InterruptedException e) {
                log.error("RpcServerConfig-exporter exception:{}", e);
                e.printStackTrace();
            }

            providerConfigs.forEach(config -> {
                rpcServer.registerProcessor(config);
            });

            //TODO 待扩展... 后续可以注册到Zookeeper中

        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<ProviderConfig> getProviderConfigs() {
        return providerConfigs;
    }

    public void setProviderConfigs(List<ProviderConfig> providerConfigs) {
        this.providerConfigs = providerConfigs;
    }
}
