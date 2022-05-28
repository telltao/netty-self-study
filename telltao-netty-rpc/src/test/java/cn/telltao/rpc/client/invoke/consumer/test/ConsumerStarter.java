package cn.telltao.rpc.client.invoke.consumer.test;

import cn.telltao.rpc.client.RpcClient;
import cn.telltao.rpc.client.RpcFuture;
import cn.telltao.rpc.client.proxy.RpcAsyncProxy;
import cn.telltao.rpc.client.invoke.consumer.test.HelloService;
import java.util.concurrent.ExecutionException;


public class ConsumerStarter {

	 static String address = "127.0.0.1:9000";


	public static void sync() {

        System.out.println("RPC开始[同步]调用");

        //	rpcClient
        RpcClient rpcClient = new RpcClient();
        rpcClient.initClient(address, 3000);
        HelloService helloService = rpcClient.invokeSync(HelloService.class);
        String result = helloService.hello("毛毛");
        System.out.println("sync-result:" + result);
    }

    public static void async() throws InterruptedException, ExecutionException {
        System.out.println("RPC开始[异步]调用");
        RpcClient rpcClient = new RpcClient();
        rpcClient.initClient(address, 3000);
        RpcAsyncProxy proxy = rpcClient.invokeAsync(HelloService.class);
        RpcFuture future = proxy.asyncCall("hello", "毛毛");
        RpcFuture future2 = proxy.asyncCall("hello", new User("1", "毛毛"));

        Object result = future.get();
        Object result2 = future2.get();
        System.err.println("async-result: " + result);
        System.err.println("async--result: " + result2);

    }

    public static void main(String[] args) throws Exception {
        sync();
        //async();

    }
}
