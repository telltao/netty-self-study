package cn.telltao.rpc.client;

/**
 * @author telltao@qq.com
 * 初始化RP客户端
 * @Date 2021/7/23 18:22
 */
public class RpcClient {

    private String serverAddress;

    private long timeout;

    public void initClient(String serverAddress, long timeout) {
        this.serverAddress = serverAddress;
        this.timeout = timeout;
        connect();

    }


    private void connect() {
        RpcConnectManager.getInstance().connect(serverAddress);
    }

    public void stop() {
        RpcConnectManager.getInstance().stop();
    }

}
