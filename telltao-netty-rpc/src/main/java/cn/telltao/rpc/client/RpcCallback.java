package cn.telltao.rpc.client;

/**
 * @author telltao@qq.com
 * RPC回调接口
 * @Date 2021/7/25 11:30
 */
public interface RpcCallback {

    void sueecess(Object result);

    void failure(Throwable throwable);
}
