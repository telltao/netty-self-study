package cn.telltao.rpc.client.proxy;

import cn.telltao.rpc.client.RpcFuture;

/**
 * @author telltao@qq.com
 * 异步代理接口
 * @Date 2021/7/26 12:53
 */
public interface RpcAsyncProxy {


    /**
     * <pre>
     * @author telltao@qq.com
     *  异步接口回调接口
     * </pre>
     *
     * @date 2021/7/26 13:12
     */
    RpcFuture asyncCall(String functionName, Object... args);


}
