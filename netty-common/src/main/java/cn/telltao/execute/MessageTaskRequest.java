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
                (module, cmd, result.getResultType(), result.getContent());

        ctx.writeAndFlush(responseMessage);
    }
}
