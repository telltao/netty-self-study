package cn.telltao.rpc.client.invoke.consumer.test;

import cn.telltao.rpc.client.RpcClient;
import cn.telltao.rpc.client.RpcFuture;
import cn.telltao.rpc.client.proxy.RpcAsyncProxy;

import java.util.concurrent.ExecutionException;


public class ConsumerStarter {

	 static String address = "127.0.0.1:9000";


	public static void sync() {

        //	rpcClient
        RpcClient rpcClient = new RpcClient();
        rpcClient.initClient(address, 3000);
        HelloService helloService = rpcClient.invokeSync(HelloService.class);
        String result = helloService.hello("zhang3");
        System.err.println(result);
    }

    public static void async() throws InterruptedException, ExecutionException {
        RpcClient rpcClient = new RpcClient();
        rpcClient.initClient(address, 3000);
        RpcAsyncProxy proxy = rpcClient.invokeAsync(HelloService.class);
        RpcFuture future = proxy.asyncCall("hello", "毛毛");
        RpcFuture future2 = proxy.asyncCall("hello", new User("1", "毛毛"));

        Object result = future.get();
        Object result2 = future2.get();
        System.err.println("result: " + result);
        System.err.println("result2: " + result2);

    }

    public static void main(String[] args) throws Exception {
        async();
    }
}
