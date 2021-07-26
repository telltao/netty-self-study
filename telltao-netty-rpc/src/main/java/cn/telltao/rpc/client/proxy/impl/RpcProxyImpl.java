package cn.telltao.rpc.client.proxy.impl;

import cn.telltao.rpc.client.RpcClientHandler;
import cn.telltao.rpc.client.RpcConnectManager;
import cn.telltao.rpc.client.RpcFuture;
import cn.telltao.rpc.client.proxy.RpcAsyncProxy;
import cn.telltao.rpc.codec.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * 同步,异步代理调用类
 * @Date 2021/7/26 12:29
 */
public class RpcProxyImpl<T> implements InvocationHandler, RpcAsyncProxy {

    private Class<T> clazz;

    private long timeout;

    private RpcConnectManager rpcConnectManager;

    public RpcProxyImpl(RpcConnectManager rpcConnectManager, Class<T> clazz, long timeout) {
        this.clazz = clazz;
        this.timeout = timeout;
        this.rpcConnectManager = rpcConnectManager;

    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  代理接口调用方式
     * </pre>
     *
     * @date 2021/7/26 12:50
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生成RPC代理对象,并返回

        // 1,设置请求对象
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamters(args);
        request.setParamterTypes(method.getParameterTypes());

        //2,选择一个合适的Client对象
        RpcClientHandler clientHandler = this.rpcConnectManager.chooseHandler();

        //3,发送客户端请求,并返回结果
        RpcFuture rpcFuture = clientHandler.sendRequest(request);
        //同步阻塞,底层是AQS实现(看源码吧..)
        Object responseObject = rpcFuture.get(timeout, TimeUnit.SECONDS);

        return responseObject;
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  异步代理接口实现,并调用实际接口等待处理
     * </pre>
     *
     * @date 2021/7/26 13:11
     */
    @Override
    public RpcFuture asyncCall(String functionName, Object... args) {

        // 1,设置请求对象
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(this.clazz.getName());
        request.setMethodName(functionName);
        request.setParamters(args);

        //if (args.length > 0) {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = getClassType(args[i]);
            }
            request.setParamterTypes(paramTypes);
        //}
        //调用直接客户端.发送数据并返回
        RpcClientHandler rpcClientHandler = rpcConnectManager.chooseHandler();
        RpcFuture rpcFuture = rpcClientHandler.sendRequest(request);


        return rpcFuture;
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  异步代理方法
     * </pre>
     *
     * @param
     * @return
     * @date 2021/7/26 12:57
     */
    public <T> RpcAsyncProxy invokeAsync(Class<T> interfaceClass) {

        RpcProxyImpl asyncProxyImpl = new RpcProxyImpl(rpcConnectManager,interfaceClass, timeout);

        return asyncProxyImpl;
    }

    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        if (typeName.equals("java.lang.Integer")) {
            return Integer.TYPE;
        } else if (typeName.equals("java.lang.Long")) {
            return Long.TYPE;
        } else if (typeName.equals("java.lang.Float")) {
            return Float.TYPE;
        } else if (typeName.equals("java.lang.Double")) {
            return Double.TYPE;
        } else if (typeName.equals("java.lang.Character")) {
            return Character.TYPE;
        } else if (typeName.equals("java.lang.Boolean")) {
            return Boolean.TYPE;
        } else if (typeName.equals("java.lang.Short")) {
            return Short.TYPE;
        } else if (typeName.equals("java.lang.Byte")) {
            return Byte.TYPE;
        }
        return classType;
    }
}
