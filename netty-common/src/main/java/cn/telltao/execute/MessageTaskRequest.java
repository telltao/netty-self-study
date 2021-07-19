package cn.telltao.execute;

import cn.telltao.common.protobuf.MessageBuilder;
import cn.telltao.common.protobuf.MessageModule;
import cn.telltao.common.protobuf.Result;
import cn.telltao.common.scanner.Invoker;
import cn.telltao.common.scanner.InvokerTable;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author telltao@qq.com
 * @Date 2021/7/18 22:18
 */
public class MessageTaskRequest implements Runnable {

    private MessageModule.Message message;

    private ChannelHandlerContext ctx;

    private final static String  RETURN = "-return";

    public MessageTaskRequest(MessageModule.Message message, ChannelHandlerContext ctx) {
        this.message = message;
        this.ctx = ctx;
    }


    @Override
    public void run() {

        String module = message.getModule();
        String cmd = message.getCmd();
        byte[] data = message.getBody().toByteArray();

        Invoker invoker = InvokerTable.getInvoker(module, cmd);
        Result<?> result = (Result<?>) invoker.invoke(data);

        Message responseMessage = MessageBuilder.getResponseMessage
                (module +RETURN, cmd +RETURN, result.getResultType(), result.getContent());

        ctx.writeAndFlush(responseMessage);
        // 此处为何不用执行? //释放资源    ReferenceCountUtil.release(message);
        //因为服务端给客户端写数据时,会自动帮你进行释放,请看 writeAndFlush源码
    }
}
