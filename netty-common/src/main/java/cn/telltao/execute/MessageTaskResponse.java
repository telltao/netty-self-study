package cn.telltao.execute;

import cn.telltao.common.protobuf.MessageModule;
import cn.telltao.common.scanner.Invoker;
import cn.telltao.common.scanner.InvokerTable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author telltao@qq.com
 * @Date 2021/7/19 14:32
 */
public class MessageTaskResponse implements Runnable {

    private MessageModule.Message message;

    private ChannelHandlerContext ctx;

    public MessageTaskResponse(MessageModule.Message message, ChannelHandlerContext ctx) {
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            String module = this.message.getModule();
            String cmd = this.message.getCmd();
            //响应结果
            MessageModule.ResultType resultType = this.message.getResultType();
            //响应内容
            byte[] bytes = this.message.getBody().toByteArray();

            Invoker invoker = InvokerTable.getInvoker(module, cmd);

            invoker.invoke(resultType, bytes);

        } finally {
            //释放资源
            ReferenceCountUtil.release(message);
        }
    }
}
