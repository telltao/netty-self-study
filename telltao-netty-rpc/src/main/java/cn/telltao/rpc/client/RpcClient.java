package cn.telltao.rpc.client;

import cn.telltao.rpc.client.proxy.RpcAsyncProxy;
import cn.telltao.rpc.client.proxy.impl.RpcProxyImpl;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author telltao@qq.com
 * 初始化RP客户端
 * @Date 2021/7/23 18:22
 */
public class RpcClient {

    private String serverAddress;

    private long timeout;

    private final Map<Class<?>, Object> syncProxyIntanceMap = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> asyncProxyIntanceMap = new ConcurrentHashMap<>();

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

    /**
     * <pre>
     * @author telltao@qq.com
     *  同步调用方法
     *  使用动态代理生成并返回
     *  返回指定接口的代理实例，该接口将方法调用分派到指定的调用处理程序。
     * </pre>
     *
     * @date 2021/7/26 12:38
     */
    public <T> T invokeSync(Class<T> interfaceClass) {

        if (syncProxyIntanceMap.containsKey(interfaceClass)) {
            return (T) syncProxyIntanceMap.get(interfaceClass);
        }

        Object proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxyImpl<>(interfaceClass, timeout));

        syncProxyIntanceMap.put(interfaceClass, proxy);
        return (T) proxy;

    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  异步调用方法
     * </pre>
     *
     * @date 2021/7/26 12:38
     */
    public <T> RpcAsyncProxy invokeAsync(Class<T> interfaceClass) {

        if (asyncProxyIntanceMap.containsKey(interfaceClass)) {
            return (RpcAsyncProxy) asyncProxyIntanceMap.get(interfaceClass);
        } else {
            RpcProxyImpl<T> asyncProxImpl = new RpcProxyImpl<>(interfaceClass, timeout);
            asyncProxyIntanceMap.put(interfaceClass, asyncProxImpl);
            return asyncProxImpl;
        }
    }


}
