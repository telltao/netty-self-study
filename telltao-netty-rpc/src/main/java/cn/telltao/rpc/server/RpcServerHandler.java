package cn.telltao.rpc.server;

import cn.telltao.rpc.codec.RpcRequest;
import cn.telltao.rpc.codec.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author telltao@qq.com
 * RPC服务端的handler
 * @Date 2021/7/24 14:06
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> handlerMap;

    private ThreadPoolExecutor executor =
            new ThreadPoolExecutor(16, 16, 600L,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        //1,解析RPC request
        //2,从map根据key找到对应的实例
        //3,通过反射进行cglib调用 具体方法,传递相关执行参数并执行
        //4,返回响应信息给调用者

        executor.submit(() -> {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());

            try {
                Object handle = handle(request);
                response.setResult(handle);
            } catch (Exception e) {
                //返回异常
                response.setThrowable(e);
                log.error("rpc service handle requesty Throwable:{}", t);
            }
            ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // afterRpcHook
                    }
                }
            });
        });
    }

    /**
     * <pre>
     * @author telltao@qq.com
     * 解析request请求,并通过反射去获取具体的本地服务,然后执行相应方法,执行后并返回
     * 关键字: 反射,cglib,jdk动态代理
     * </pre>
     *
     * @param
     * @return
     * @date 2021/7/25 11:13
     */
    private Object handle(RpcRequest request) throws InvocationTargetException {
        String className = request.getClassName();
        Object ref = handlerMap.get(className);
        Class<?> serviceClass = ref.getClass();
        String methodName = request.getMethodName();
        Class<?>[] paramtersType = request.getParamterTypes();
        Object[] paramters = request.getParamters();

        //JDK relect

        //Cglib
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastClassMethod = fastClass.getMethod(methodName, paramtersType);
        Object invokeResult = fastClassMethod.invoke(ref, paramters);

        return invokeResult;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server exception  caught Throwable:{}", cause);
        ctx.close();
    }
}
